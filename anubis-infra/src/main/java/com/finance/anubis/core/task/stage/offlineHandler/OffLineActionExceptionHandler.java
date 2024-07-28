package com.finance.anubis.core.task.stage.offlineHandler;


import com.aliyun.openservices.ons.api.Message;
import com.finance.anubis.core.constants.Constants;
import com.finance.anubis.core.task.model.OffLineTaskActivity;
import com.finance.anubis.exception.ErrorMsg;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.finance.anubis.repository.mq.OffLineActionMqBody;
import com.guming.api.json.JsonUtil;
import com.guming.common.exception.StatusCodeException;
import com.finance.anubis.core.util.DingTalkWebhookUtil;
import com.guming.mq.api.MessageProducer;
import com.guming.mq.base.MessageBuilder;
import lombok.CustomLog;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@CustomLog
public class OffLineActionExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final MessageProducer messageProducer;

    private final DingTalkWebhookUtil dingTalkWebhookUtil;

    private final OffLineTaskActivityRepository activityRepository;

    static Map<Integer, Integer> retryTimeMap = new HashMap<>();

    public OffLineActionExceptionHandler(MessageProducer messageProducer, DingTalkWebhookUtil dingTalkWebhookUtil, OffLineTaskActivityRepository activityRepository) {
        this.messageProducer = messageProducer;
        this.dingTalkWebhookUtil = dingTalkWebhookUtil;
        this.activityRepository = activityRepository;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        StatusCodeException exception = null;
        if (e instanceof StatusCodeException) {
            exception = (StatusCodeException) e;
        } else {
            log.error("uncaughtException ", e);
            dingTalkWebhookUtil.sendAtAllMarkDown("对账平台离线对账错误,未捕获异常", e.getMessage());
        }
        String message = exception.getStatusCode().getReason();
        ErrorMsg msg = JsonUtil.of(message, ErrorMsg.class);
        log.error("ExceptionHandler caught exception:{}", msg);
        OffLineTaskActivity activity = activityRepository.getByBizKey(msg.getBizKey());
        Integer retryTime = activity.getTaskConfig().getRetryTime();
        OffLineActionMqBody dto = new OffLineActionMqBody(msg.getBizKey(), msg.getConfigKey());
        if (activity.getTimes() <= retryTime) {
//            dingTalkWebhookUtil.sendAtAllMarkDown("对账平台离线对账异常,重试中", "taskName:" + msg.getTaskName() + ",errorMsg:" + msg.getErrorMessage());
            log.info("对账平台离线对账异常,重试中,bizKey:{}",activity.getBizKey());
            activityRepository.updateTimes(activity.getId());
        } else {
            dingTalkWebhookUtil.sendAtAllMarkDown("对账平台离线对账失败", "bizKey:" + msg.getBizKey());
            activityRepository.toFail(activity.getId(), activity.getAction());
        }
        Message mqMessage = MessageBuilder.create()
                .topic(Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC)
                .tag(msg.getAction().getCode())
                .body(dto)
                .build();
        messageProducer.sendDelayMsg(mqMessage, fibonacci(activity.getTimes()), TimeUnit.SECONDS);
    }

    public static int fibonacci(int n) {
        if (retryTimeMap.containsKey(n)) {
            return retryTimeMap.get(n);
        }
        if (n == 1 || n == 2) {
            return 5;
        }
        if (n > 10) {
            return fibonacci(10);
        }
        if (n > 2) {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
        return 0;
    }

}