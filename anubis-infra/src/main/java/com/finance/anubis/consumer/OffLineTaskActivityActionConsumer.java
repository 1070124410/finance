package com.finance.anubis.consumer;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.finance.anubis.constants.Constants;
import com.finance.anubis.core.factory.OffLineActionHandlerFactory;
import com.finance.anubis.core.model.OffLineTaskActivity;
import com.finance.anubis.exception.Status;
import com.finance.anubis.exception.StatusCodeException;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.finance.anubis.repository.mq.OffLineActionMqBody;
import lombok.CustomLog;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

import static com.finance.anubis.constants.Constants.ANUBIS_MQ_CONSUMER_GROUP;
import static com.finance.anubis.constants.Constants.TASK_ACTIVITY_ACTION_TOPIC;

/**
 * @Author yezhaoyang
 * @Date 2023/03/01 20:16
 * @Description
 **/
@RocketMQMessageListener(
        topic = TASK_ACTIVITY_ACTION_TOPIC,
        consumerGroup = ANUBIS_MQ_CONSUMER_GROUP,
        selectorExpression = "DATA_INIT || DATA_FETCH || DATA_COMPARE_TOTAL || DATA_COMPARE_DETAIL || DATA_DONE || DATA_FAIL",
        messageModel = MessageModel.CLUSTERING
)
public class OffLineTaskActivityActionConsumer implements MessageListener {

    public final static Logger log = LoggerFactory.getLogger(OffLineTaskActivityActionConsumer.class);
    private final OffLineActionHandlerFactory offLineActionHandlerFactory;

    private final OffLineTaskActivityRepository activityRepository;

    private final Executor executor;

    public OffLineTaskActivityActionConsumer(OffLineActionHandlerFactory offLineActionHandlerFactory,
                                             OffLineTaskActivityRepository activityRepository, Executor executor) {
        this.offLineActionHandlerFactory = offLineActionHandlerFactory;
        this.activityRepository = activityRepository;
        this.executor = executor;
    }


    @Override
    public Action consume(Message message, ConsumeContext context) {
        log.info("consumer offlineTask,topic:{},tag:{},msgId:{}", message.getTopic(), message.getTag(), message.getMsgID());
        String jsonBody = null;
        Entry entry = null;
        try {
            entry = SphU.entry(Constants.TASK_ACTIVITY_ACTION_TOPIC + Constants.UNDERLINE_SPLIE + Constants.OFFLINE_TASK_CONSUMER_GROUP);
            jsonBody = new String(message.getBody());
            OffLineActionMqBody msgBody = JSONUtil.toBean(jsonBody, OffLineActionMqBody.class);
            OffLineTaskActivity activity = activityRepository.getByBizKey(msgBody.getBizKey());
            if (activity == null) {
                throw new StatusCodeException(Status.error("taskActivity not exist"));
            }
            //异步执行
            executor.execute(() -> {
                offLineActionHandlerFactory.getOffLineHandler(activity.getAction()).handle(activity, msgBody.getConfigKey());
            });

            return Action.CommitMessage;
        } catch (BlockException e) {
            log.warn("Topic:{} consume has been limiting", Constants.TASK_ACTIVITY_ACTION_TOPIC);
            return Action.ReconsumeLater;
        } catch (Exception e) {
            log.error("consume offLineTask activity failed,body: [{}]", jsonBody, e);
            return Action.ReconsumeLater;
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

}
