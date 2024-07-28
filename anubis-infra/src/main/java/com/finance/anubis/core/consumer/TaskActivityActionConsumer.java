package com.finance.anubis.core.consumer;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.finance.anubis.core.constants.Constants;
import com.finance.anubis.core.task.model.TaskActivity;
import com.finance.anubis.core.task.model.TaskActivityResult;
import com.finance.anubis.repository.TaskActivityRepository;
import com.finance.anubis.core.factory.ActionHandlerFactory;
import com.guming.mq.annotation.MQConsumer;
import lombok.CustomLog;

/**
 * 比对action 监听
 */
@CustomLog
@MQConsumer(group = Constants.ANUBIS_MQ_CONSUMER_GROUP, topic = Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC, tag = {
        "INIT", "SOURCE_FETCH", "TARGET_FETCH", "REMARK", "COMPARE"
})
public class TaskActivityActionConsumer implements MessageListener {

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
            entry = SphU.entry(Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC + Constants.UNDERLINE_SPLIE + Constants.ANUBIS_MQ_CONSUMER_GROUP);
            jsonBody = new String(message.getBody());
            TaskActivityResult taskActivityResult = JSONUtil.toBean(jsonBody, TaskActivityResult.class);
            if (taskActivityResult.getTaskActivityId() != null) {
                // 如果当前任务在比对中，则执行，如果完成则忽略，重试activity需要标记为remark
                TaskActivity taskActivity = taskActivityRepository.getById(taskActivityResult.getTaskActivityId());
                if (taskActivity != null && taskActivity.getAction() != com.finance.anubis.core.constants.enums.Action.DONE) {
                    actionHandlerFactory.getHandler(taskActivity.getAction()).handle(taskActivity);
                } else {
                    log.error("consume error task activity action {}", jsonBody);
                }
            } else {
                log.error("consume error task activity action {}", jsonBody);
            }

            return Action.CommitMessage;
        } catch (BlockException e) {
            log.warn("Topic:{} consume has been limiting", Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC);
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
