package com.finance.anubis.core.task.runner;

import cn.hutool.core.bean.BeanUtil;
//import com.alibaba.csp.sentinel.Entry;
//import com.alibaba.csp.sentinel.SphU;
//import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageType;
import com.finance.anubis.repository.TaskActivityRepository;
import com.finance.anubis.core.config.EventResourceConfig;
import com.finance.anubis.core.config.MessageResourceConfig;
import com.finance.anubis.core.config.OnLineTaskConfig;
import com.finance.anubis.core.constants.enums.ResourceType;
import com.finance.anubis.core.context.ActivityContext;
import com.finance.anubis.core.task.executor.SourceExecutor;
import com.finance.anubis.core.task.model.TaskActivity;
import com.guming.mq.rocket.DecorateUtils;
import com.guming.mq.rocket.consumer.BeanConsumerContext;
import com.guming.mq.rocket.consumer.BridgeConsumerClient;
import com.guming.mq.rocket.consumer.ConsumerHolder;
import lombok.CustomLog;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@CustomLog
public class MQSourceRunner implements SourceRunner {


    MQConsumer mqConsumer;

    SourceExecutor sourceExecutor;

    private final BridgeConsumerClient consumerClient;

    private final MessageResourceConfig messageResourceConfig;


    private static class ConsumerClientHolder {
        public static BridgeConsumerClient INSTANCE = new BridgeConsumerClient();
    }


    public MQSourceRunner(MQConsumer mqConsumer,
                          MessageResourceConfig messageResourceConfig) {
        this.mqConsumer = mqConsumer;
        this.messageResourceConfig = messageResourceConfig;
        this.consumerClient = ConsumerClientHolder.INSTANCE;
    }


    public void remark(TaskActivity taskActivity) {
        taskActivity.remark();
        sourceExecutor.handle(taskActivity);
    }

    @Override
    public void start() {
        String group = null;
        List<String> tags = new ArrayList<>();
        String topic = null;
        String messageModel = null;
        // 初始化consumer配置
        if (messageResourceConfig != null) {
            group = messageResourceConfig.getGroup();
            tags = messageResourceConfig.getTags();
            topic = messageResourceConfig.getTopic();
            messageModel = messageResourceConfig.getMessageMode();
        }
        // 初始化消费者
        initConsumerListener(mqConsumer, topic, group, tags.toArray(new String[0]), messageModel);
    }


    private void initConsumerListener(Object bean, String topic, String group, String[] tags, String messageMode) {
        if (bean instanceof MessageListener) {
            //普通消息
            BeanConsumerContext consumerContext = buildConsumerContext(topic, tags, group, bean, MessageType.Normal_Msg, messageMode);
            BeanForwardingMessageListener listener = new BeanForwardingMessageListener(consumerContext);
            consumerClient.initializeConsumer(consumerContext, listener);
        } else if (bean instanceof MessageOrderListener) {
            //顺序消息
            BeanConsumerContext consumerContext = buildConsumerContext(topic, tags, group, bean, MessageType.Order_Msg, messageMode);
            BeanForwardingMessageOrderListener listener = new BeanForwardingMessageOrderListener(consumerContext);
            consumerClient.initializeConsumer(consumerContext, listener);
        } else {
            throw new RuntimeException("消息监听器必须继承于MessageListener或者MessageOrderListener，bean：" + bean.getClass().getName());
        }
    }

    private BeanConsumerContext buildConsumerContext(String topic, String[] tags, String group, Object bean, MessageType normal_Msg, String messageMode) {
        BeanConsumerContext consumerContext = new BeanConsumerContext(topic, tags, group, bean);
        consumerContext.setMessageType(normal_Msg.getShortName());
        consumerContext.setMessageMode(messageMode);
        return consumerContext;
    }

    @Override
    public void stop() {
        ConsumerHolder consumerHolder = (ConsumerHolder) BeanUtil.getFieldValue(consumerClient, "consumerHolder");
        String groupId = DecorateUtils.decorateGroup(messageResourceConfig.getGroup());
        ConsumerHolder.ConsumerWrap consumerWrap = consumerHolder.getConsumer(groupId);
        ConcurrentHashMap<String, MessageListener> subscribeTable = (ConcurrentHashMap<String, MessageListener>) BeanUtil.getFieldValue(consumerWrap, "subscribeTable");
        String topicId = DecorateUtils.decorateTopic(messageResourceConfig.getTopic());
        subscribeTable.remove(topicId);
        Consumer consumer = (Consumer) BeanUtil.getFieldValue(consumerWrap, "consumer");
        consumer.unsubscribe(topicId);
    }


    @CustomLog
    public static class MQConsumer implements MessageListener {

        @Getter
        private final SourceExecutor sourceExecutor;

        @Getter
        private final String groupId;

        @Getter
        private final OnLineTaskConfig onLineTaskConfig;
        @Getter
        private final Long taskId;

        @Getter
        private final TaskActivityRepository taskActivityRepository;

        @Getter
        private final String resourceKey;

        public MQConsumer(
                TaskActivityRepository taskActivityRepository,
                SourceExecutor sourceExecutor,
                OnLineTaskConfig onLineTaskConfig,
                String groupId,
                Long taskId) {
            this.sourceExecutor = sourceExecutor;
            this.onLineTaskConfig = onLineTaskConfig;
            this.groupId = groupId;
            this.taskId = taskId;
            this.taskActivityRepository = taskActivityRepository;
            resourceKey = onLineTaskConfig.getName() + "_MQ";
        }

        @Override
        public Action consume(Message message, ConsumeContext context) {
            Entry entry = null;
            String messageBodyStr = null;
            try {
                entry = SphU.entry(resourceKey);
                messageBodyStr = new String(message.getBody());
                MessageResourceConfig sourceConfig = onLineTaskConfig.getSourceConfig();
                ActivityContext activityContext = new ActivityContext();
                activityContext.setTaskId(taskId);
                ResourceType resourceType = onLineTaskConfig.getSourceConfig().getResourceType();
                ActivityContext.SourceContext sourceContext =
                        resourceType == ResourceType.EventResourceConfig ?
                                ActivityContext.EventSourceContext.init(messageBodyStr, (EventResourceConfig) sourceConfig) :
                                ActivityContext.MessageSourceContext.init(messageBodyStr, sourceConfig);
                activityContext.setSourceContext(sourceContext);

                TaskActivity taskActivity = taskActivityRepository.getByBizKey(activityContext.getBizKey(onLineTaskConfig.getUniqueKeys(), onLineTaskConfig.getName()));
                if (taskActivity == null) {
                    taskActivity = TaskActivity.init();
                    taskActivity.setContext(activityContext);
                    taskActivity.setOnLineTaskConfig(onLineTaskConfig);
                } else {
                    if (taskActivity.getAction() != com.finance.anubis.core.constants.enums.Action.DONE) {
                        return com.aliyun.openservices.ons.api.Action.CommitMessage;
                    }

                    taskActivity.remark();
                    taskActivity.setContext(activityContext);
                }

                sourceExecutor.handle(taskActivity);
                return com.aliyun.openservices.ons.api.Action.CommitMessage;
            } catch (BlockException e) {
                log.warn("taskName:{} mq consume has been limiting", onLineTaskConfig.getName());
                return com.aliyun.openservices.ons.api.Action.ReconsumeLater;
            } catch (Exception e) {
                log.error("consumer message failed. taskId: {} message body: {}", taskId, messageBodyStr, e);
                return com.aliyun.openservices.ons.api.Action.ReconsumeLater;
            } finally {
                if (entry != null) {
                    entry.exit();
                }
            }

        }
    }

    private static class BeanForwardingMessageListener implements MessageListener {

        private final BeanConsumerContext consumerContext;

        public BeanForwardingMessageListener(BeanConsumerContext consumerContext) {
            this.consumerContext = consumerContext;
        }

        @Override
        public Action consume(Message message, ConsumeContext context) {
            return ((MessageListener) consumerContext.getBean()).consume(message, context);
        }
    }

    /**
     * 顺序消息转发
     */
    private static class BeanForwardingMessageOrderListener implements MessageOrderListener {

        private final BeanConsumerContext consumerContext;

        public BeanForwardingMessageOrderListener(BeanConsumerContext consumerContext) {
            this.consumerContext = consumerContext;
        }

        @Override
        public OrderAction consume(Message message, ConsumeOrderContext context) {
            return ((MessageOrderListener) consumerContext.getBean()).consume(message, context);
        }
    }
}
