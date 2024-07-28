package com.finance.anubis.core.task.stage.offlineHandler;

import com.aliyun.openservices.ons.api.Message;
import com.finance.anubis.core.task.model.OffLineTaskActivity;
import com.finance.anubis.repository.OffLineActivityResultRepository;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.finance.anubis.repository.mq.OffLineActionMqBody;
import com.finance.anubis.core.constants.Constants;
import com.finance.anubis.core.constants.enums.OffLineAction;
import com.finance.anubis.core.constants.enums.StatusResult;
import com.finance.anubis.core.task.model.OffLineActivityResult;
import com.guming.mq.api.MessageProducer;
import com.guming.mq.base.MessageBuilder;
import lombok.CustomLog;
import org.springframework.stereotype.Component;

import static com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_DETAIL;
import static com.finance.anubis.core.constants.enums.OffLineAction.DATA_COMPARE_TOTAL;


@CustomLog
@Component
public class DataCompareTotalActionHandler extends OffLineActionHandler {
    private final OffLineTaskActivityRepository taskActivityRepository;

    private final OffLineActivityResultRepository activityResultRepository;

    private final MessageProducer messageProducer;

    public DataCompareTotalActionHandler(OffLineTaskActivityRepository taskActivityRepository, MessageProducer messageProducer,
                                         OffLineActivityResultRepository activityResultRepository) {
        super(OffLineAction.DATA_COMPARE_TOTAL);
        this.taskActivityRepository = taskActivityRepository;
        this.activityResultRepository = activityResultRepository;
        this.messageProducer = messageProducer;
    }

    @Override
    protected StatusResult checkStatus(OffLineTaskActivity taskActivity, String key) {
        OffLineTaskActivity activity = taskActivityRepository.getByBizKey(taskActivity.getBizKey());
        if (activity.getAction().equals(DATA_COMPARE_TOTAL)) {
            return StatusResult.ACT;
        }
        return StatusResult.IGNORE;
    }

    @Override
    protected void innerHandle(OffLineTaskActivity taskActivity, String key) {
        OffLineActivityResult activityResult = taskActivity.verifyNumericalTotal();
        taskActivity.setActivityResult(activityResult);
        activityResultRepository.save(taskActivity.getActivityResult());
    }

    @Override
    protected void updateActivity(OffLineTaskActivity taskActivity, String key) {
        taskActivityRepository.toDataCompareDetail(taskActivity.getId(), taskActivity.getAction());
    }

    @Override
    protected void afterHandle(OffLineTaskActivity taskActivity, String key) {
        OffLineActionMqBody msgBody = new OffLineActionMqBody(taskActivity.getBizKey(), key);
        Message message = MessageBuilder.create().topic(Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC)
                .tag(DATA_COMPARE_DETAIL.getCode())
                .body(msgBody)
                .build();
        messageProducer.syncSend(message);
    }

}
