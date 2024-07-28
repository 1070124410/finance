package com.finance.anubis.core.task.stage.offlineHandler;

import com.finance.anubis.core.model.OffLineTaskActivity;
import com.finance.anubis.enums.StatusResult;
import com.finance.anubis.model.OffLineActivityResult;
import com.finance.anubis.mq.MessageProducer;
import com.finance.anubis.repository.OffLineActivityResultRepository;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.finance.anubis.repository.mq.OffLineActionMqBody;
import com.finance.anubis.utils.JsonUtil;
import lombok.CustomLog;
import org.springframework.stereotype.Component;

import static com.finance.anubis.constants.Constants.TASK_ACTIVITY_ACTION_TOPIC;
import static com.finance.anubis.enums.OffLineAction.DATA_COMPARE_DETAIL;
import static com.finance.anubis.enums.OffLineAction.DATA_COMPARE_TOTAL;


@Component
public class DataCompareTotalActionHandler extends OffLineActionHandler {
    private final OffLineTaskActivityRepository taskActivityRepository;

    private final OffLineActivityResultRepository activityResultRepository;

    private final MessageProducer messageProducer;

    public DataCompareTotalActionHandler(OffLineTaskActivityRepository taskActivityRepository, MessageProducer messageProducer,
                                         OffLineActivityResultRepository activityResultRepository) {
        super(DATA_COMPARE_TOTAL);
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
        messageProducer.syncSend(TASK_ACTIVITY_ACTION_TOPIC, DATA_COMPARE_DETAIL.getCode(), JsonUtil.toJson(msgBody));
    }

}
