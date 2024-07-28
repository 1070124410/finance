package com.finance.anubis.core.job;

import com.finance.anubis.core.constants.enums.OffLineAction;
import com.finance.anubis.core.task.model.OffLineTaskActivity;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.finance.anubis.core.util.DingTalkWebhookUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.CustomLog;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@JobHandler("offLineTaskResultJobHandler")
@CustomLog
public class OffLineTaskResultJobHandler extends IJobHandler {

    private final OffLineTaskActivityRepository activityRepository;

    private final DingTalkWebhookUtil dingTalkWebhookUtil;


    public OffLineTaskResultJobHandler(OffLineTaskActivityRepository activityRepository, DingTalkWebhookUtil dingTalkWebhookUtil) {
        this.activityRepository = activityRepository;
        this.dingTalkWebhookUtil = dingTalkWebhookUtil;
    }


    @Override
    public ReturnT<String> execute(String param) {
        List<OffLineTaskActivity> activityList = activityRepository.selectActiveList();
        List<OffLineTaskActivity> doneList = activityList.stream().filter(a -> OffLineAction.DATA_DONE.equals(a.getAction())).collect(Collectors.toList());
        List<OffLineTaskActivity> failList = activityList.stream().filter(a -> OffLineAction.DATA_FAIL.equals(a.getAction())).collect(Collectors.toList());
        Integer processing = activityList.size() - doneList.size() - failList.size();
        dingTalkWebhookUtil.sendAtAllMarkDown("对账结果统计", "截止目前对账活跃任务个数:" + activityList.size() + ",对账通过:" + doneList.size() + ",对账失败:" + failList.size() + ",进行中:" + processing);

        return ReturnT.SUCCESS;
    }

}
