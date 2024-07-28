package com.finance.anubis.job;

import com.finance.anubis.core.model.OffLineTaskActivity;
import com.finance.anubis.core.util.DingTalkWebhookUtil;
import com.finance.anubis.enums.OffLineAction;
import com.finance.anubis.enums.SourceDataStatus;
import com.finance.anubis.mq.MessageProducer;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.finance.anubis.repository.mq.OffLineActionMqBody;
import com.finance.anubis.utils.JsonUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.finance.anubis.constants.Constants.TASK_ACTIVITY_ACTION_TOPIC;

@Component
@JobHandler("taskStatusRemainJobHandler")
public class TaskStatusRemainJobHandler extends IJobHandler {
    public final static Logger log = LoggerFactory.getLogger(TaskStatusRemainJobHandler.class);
    private final OffLineTaskActivityRepository activityRepository;

    private final MessageProducer messageProducer;

    private final DingTalkWebhookUtil dingTalkWebhookUtil;

    public TaskStatusRemainJobHandler(OffLineTaskActivityRepository activityRepository,
                                      MessageProducer messageProducer, DingTalkWebhookUtil dingTalkWebhookUtil) {
        this.activityRepository = activityRepository;
        this.messageProducer = messageProducer;
        this.dingTalkWebhookUtil = dingTalkWebhookUtil;
    }


    @Override
    public ReturnT<String> execute(String param) {
        List<OffLineTaskActivity> activityList = activityRepository.selectActiveList();
        List<OffLineTaskActivity> taskActivities = activityList.stream().filter(a -> OffLineAction.DATA_FETCH.equals(a.getAction())).collect(Collectors.toList());
        for (OffLineTaskActivity activity : taskActivities) {
            Set<String> keySet = activity.getContext().getSourceContext().keySet();
            for (String key : keySet) {
                SourceDataStatus status = activity.getSourceContext(key).getSourceDataStatus();
                if (status != null && SourceDataStatus.DATA_FETCH.equals(status)) {
                    LocalDateTime fetchingTime = activity.getSourceContext(key).getFetchingTime();
                    if (fetchingTime == null) {
                        log.info("task is data_fetch but no fetching time,taskName:{},key:{}", activity.getTaskConfig().getName(), key);
                        continue;
                    }
                    Long fetchDelay = activity.getResouceConfig(key).getFetchDelay();
                    Long difference = Duration.between(fetchingTime, LocalDateTime.now()).toMinutes();
                    if (difference > fetchDelay) {
//                        dingTalkWebhookUtil.sendAtAllMarkDown("离线对账任务数据准备超时", "bizKey:" + activity.getBizKey() + ",key:" + key);
                        activityRepository.toFail(activity.getId(), activity.getAction());
                        OffLineActionMqBody dto = new OffLineActionMqBody(activity.getBizKey(), key);
                        messageProducer.syncSend(TASK_ACTIVITY_ACTION_TOPIC, OffLineAction.DATA_FAIL.getCode(), JsonUtil.toJson(dto));
                    }
                }
            }
        }

        return ReturnT.SUCCESS;
    }

}
