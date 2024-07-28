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
import com.finance.anubis.core.factory.OffLineActionHandlerFactory;
import com.finance.anubis.core.task.model.OffLineTaskActivity;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.finance.anubis.repository.mq.OffLineActionMqBody;
import com.guming.api.pojo.Status;
import com.guming.common.exception.StatusCodeException;
import com.guming.mq.annotation.MQConsumer;
import lombok.CustomLog;

import java.util.concurrent.Executor;

/**
 * @Author yezhaoyang
 * @Date 2023/03/01 20:16
 * @Description
 **/
@CustomLog
@MQConsumer(group = Constants.OFFLINE_TASK_CONSUMER_GROUP, topic = Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC, tag = {
        "DATA_INIT", "DATA_FETCH", "DATA_COMPARE_TOTAL", "DATA_COMPARE_DETAIL", "DATA_DONE", "DATA_FAIL"
})
public class OffLineTaskActivityActionConsumer implements MessageListener {

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
            entry = SphU.entry(Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC + Constants.UNDERLINE_SPLIE + Constants.OFFLINE_TASK_CONSUMER_GROUP);
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
            log.warn("Topic:{} consume has been limiting", Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC);
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
