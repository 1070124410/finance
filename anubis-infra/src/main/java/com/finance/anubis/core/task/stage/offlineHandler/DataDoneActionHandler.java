package com.finance.anubis.core.task.stage.offlineHandler;

import com.aliyun.openservices.ons.api.Message;
import com.finance.anubis.core.constants.Constants;
import com.finance.anubis.core.constants.enums.OffLineAction;
import com.finance.anubis.core.constants.enums.StatusResult;
import com.finance.anubis.core.task.model.OffLineTaskActivity;
import com.finance.anubis.core.util.FileUtil;
import com.finance.anubis.dto.OffLineTaskActivityResultDTO;
import com.finance.anubis.dto.OffLineTaskDetailResultDTO;
import com.finance.anubis.dto.OffLineTaskTotalResultDTO;
import com.finance.anubis.repository.OffLineActivityResultRepository;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.guming.mq.api.MessageProducer;
import com.guming.mq.base.MessageBuilder;
import lombok.CustomLog;
import org.springframework.stereotype.Component;

import static com.finance.anubis.adapter.OffLineActivityResultAdapter.adapt2OffLineTaskDetailResultDTO;
import static com.finance.anubis.adapter.OffLineActivityResultAdapter.adapt2OffLineTaskTotalResultDTO;


@CustomLog
@Component
public class DataDoneActionHandler extends OffLineActionHandler {

    private final OffLineTaskActivityRepository taskActivityRepository;
    private final OffLineActivityResultRepository activityResultRepository;

    private final MessageProducer messageProducer;

    private final FileUtil fileUtil;


    public DataDoneActionHandler(OffLineTaskActivityRepository taskActivityRepository, MessageProducer messageProducer,
                                 OffLineActivityResultRepository activityResultRepository, FileUtil fileUtil) {
        super(OffLineAction.DATA_DONE);
        this.taskActivityRepository = taskActivityRepository;
        this.activityResultRepository = activityResultRepository;
        this.messageProducer = messageProducer;
        this.fileUtil = fileUtil;
    }

    @Override
    protected StatusResult checkStatus(OffLineTaskActivity taskActivity, String key) {
        OffLineTaskActivity activity = taskActivityRepository.getByBizKey(taskActivity.getBizKey());
        if (activity.getAction().equals(OffLineAction.DATA_DONE)) {
            return StatusResult.ACT;
        }
        return StatusResult.IGNORE;
    }

    @Override
    protected void innerHandle(OffLineTaskActivity taskActivity, String key) {
        fileUtil.deleteLocalFiles(FailFastOffLineActionHandler.waitCleanLocalFile(taskActivity));
        //清理OSS文件
//        ossUtil.delFile(taskName, taskActivity.generateFileName(sourceKey));
//        ossUtil.delFile(taskName, taskActivity.generateFileName(targetKey));
    }

    @Override
    protected void updateActivity(OffLineTaskActivity taskActivity, String key) {
        String sourceKey = taskActivity.getTaskConfig().getSourceConfig().getKey();
        String targetKey = taskActivity.getTaskConfig().getTargetConfig().getKey();
        String sourceCustom = taskActivity.getSourceContext(sourceKey).getCustom();
        String targetCustom = taskActivity.getSourceContext(targetKey).getCustom();
        taskActivity.initSourceContext();
        taskActivity.getSourceContext(sourceKey).setCustom(sourceCustom);
        taskActivity.getSourceContext(targetKey).setCustom(targetCustom);
        taskActivityRepository.resetContext(taskActivity.getId(), taskActivity.getContext());
    }

    @Override
    protected void afterHandle(OffLineTaskActivity taskActivity, String key) {
        OffLineTaskTotalResultDTO totalResult = adapt2OffLineTaskTotalResultDTO(activityResultRepository.selectTotalResult(taskActivity.getId()));
        OffLineTaskDetailResultDTO detailResult = adapt2OffLineTaskDetailResultDTO(activityResultRepository.selectDetailResult(taskActivity.getId()));
        String sourceKey = taskActivity.getTaskConfig().getSourceConfig().getKey();
        String targetKey = taskActivity.getTaskConfig().getTargetConfig().getKey();

        OffLineTaskActivityResultDTO msg = new OffLineTaskActivityResultDTO(totalResult, detailResult,
                taskActivity.getSourceContext(sourceKey).getCustom(), taskActivity.getSourceContext(targetKey).getCustom());
        Message message = MessageBuilder.create().topic(Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC)
                .tag(taskActivity.getTaskConfig().getName() + Constants.UNDERLINE_SPLIE + OffLineAction.DATA_DONE.getCode())
                .body(msg)
                .build();
        messageProducer.syncSend(message);
    }


}
