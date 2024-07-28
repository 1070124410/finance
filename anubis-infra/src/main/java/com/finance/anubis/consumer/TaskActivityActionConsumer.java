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
import com.finance.anubis.core.model.TaskActivity;
import com.finance.anubis.model.TaskActivityResult;
import com.finance.anubis.repository.TaskActivityRepository;
import com.finance.anubis.core.factory.ActionHandlerFactory;
import lombok.CustomLog;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.finance.anubis.constants.Constants.ANUBIS_MQ_CONSUMER_GROUP;
import static com.finance.anubis.constants.Constants.TASK_ACTIVITY_ACTION_TOPIC;
import static com.finance.anubis.enums.Action.DONE;

/**
 * 比对action 监听
 */
@RocketMQMessageListener(
        topic = TASK_ACTIVITY_ACTION_TOPIC,
        consumerGroup = ANUBIS_MQ_CONSUMER_GROUP,
        selectorExpression = "INIT || SOURCE_FETCH || TARGET_FETCH || REMARK || COMPARE",
        messageModel = MessageModel.CLUSTERING
)
public class TaskActivityActionConsumer implements MessageListener {
    public final static Logger log = LoggerFactory.getLogger(TaskActivityActionConsumer.class);
    private final ActionHandlerFactory actionHandlerFactory;

    private final TaskActivityRepository taskActivityRepository;

    public TaskActivityActionConsumer(ActionHandlerFactory stageHandlerFactory, TaskActivityRepository taskActivityRepository) {
        this.actionHandlerFactory = stageHandlerFactory;
        this.taskActivityRepository = taskActivityRepository;
    }

    @Override
    public Action consume(Message message, ConsumeContext context) {
        log.info("task activity action message start，TOPIC:【{}】, TAG:【{}】, MESSAGE ID:【{}】",
                message.getTopic(), message.getTag(), message.getMsgID());
        Entry entry = null;
        String jsonBody = null;
        try {
            entry = SphU.entry(TASK_ACTIVITY_ACTION_TOPIC + Constants.UNDERLINE_SPLIE + ANUBIS_MQ_CONSUMER_GROUP);
            jsonBody = new String(message.getBody());
            TaskActivityResult taskActivityResult = JSONUtil.toBean(jsonBody, TaskActivityResult.class);
            if (taskActivityResult.getTaskActivityId() != null) {
                // 如果当前任务在比对中，则执行，如果完成则忽略，重试activity需要标记为remark
                TaskActivity taskActivity = taskActivityRepository.getById(taskActivityResult.getTaskActivityId());
                if (taskActivity != null && taskActivity.getAction() != DONE) {
                    actionHandlerFactory.getHandler(taskActivity.getAction()).handle(taskActivity);
                } else {
                    log.error("consume error task activity action {}", jsonBody);
                }
            } else {
                log.error("consume error task activity action {}", jsonBody);
            }

            return Action.CommitMessage;
        } catch (BlockException e) {
            log.warn("Topic:{} consume has been limiting", TASK_ACTIVITY_ACTION_TOPIC);
            return Action.ReconsumeLater;
        } catch (Exception e) {
            log.error("consume task activity action message failed,body: [{}]", jsonBody, e);
            return Action.ReconsumeLater;
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

}
