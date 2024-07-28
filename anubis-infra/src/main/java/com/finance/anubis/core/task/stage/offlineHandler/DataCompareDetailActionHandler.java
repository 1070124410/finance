package com.finance.anubis.core.task.stage.offlineHandler;

import com.aliyun.openservices.ons.api.Message;
import com.finance.anubis.core.task.model.OffLineTaskActivity;
import com.finance.anubis.exception.ErrorMsg;
import com.finance.anubis.repository.OffLineActivityResultRepository;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.finance.anubis.repository.mq.OffLineActionMqBody;
import com.guming.api.json.JsonUtil;
import com.guming.api.pojo.Status;
import com.guming.common.exception.StatusCodeException;
import com.finance.anubis.core.constants.Constants;
import com.finance.anubis.core.constants.enums.OffLineAction;
import com.finance.anubis.core.constants.enums.StatusResult;
import com.finance.anubis.core.task.model.OffLineActivityResult;
import com.finance.anubis.core.util.FileUtil;
import com.finance.anubis.core.util.OSSUtil;
import com.guming.mq.api.MessageProducer;
import com.guming.mq.base.MessageBuilder;
import lombok.CustomLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

import static com.finance.anubis.core.constants.Constants.LocalFile.OSS_COMPARE_FILE;
import static com.finance.anubis.core.constants.enums.OffLineAction.DATA_DONE;


@CustomLog
@Component
public class DataCompareDetailActionHandler extends OffLineActionHandler {
    private final OffLineTaskActivityRepository taskActivityRepository;

    private final OSSUtil ossUtil;

    private final FileUtil fileUtil;

    private final OffLineActivityResultRepository activityResultRepository;

    private final MessageProducer messageProducer;


    public DataCompareDetailActionHandler(OffLineTaskActivityRepository taskActivityRepository, MessageProducer messageProducer,
                                          OSSUtil ossUtil, FileUtil fileUtil, OffLineActivityResultRepository activityResultRepository) {
        super(OffLineAction.DATA_COMPARE_DETAIL);
        this.taskActivityRepository = taskActivityRepository;
        this.ossUtil = ossUtil;
        this.fileUtil = fileUtil;
        this.activityResultRepository = activityResultRepository;
        this.messageProducer = messageProducer;
    }

    @Override
    protected StatusResult checkStatus(OffLineTaskActivity taskActivity, String key) {
        //检查对账开关
        if (!taskActivity.getTaskConfig().getDetailSwitch()) {
            return StatusResult.ONLY_PUSH;
        }
        String sourceKey = taskActivity.getTaskConfig().getSourceConfig().getKey();
        String targetKey = taskActivity.getTaskConfig().getTargetConfig().getKey();
        //检查对账文件均就绪
        String sourcePath = taskActivity.getSourceContext(sourceKey).getOssReconciliationFile();
        String targetPath = taskActivity.getSourceContext(targetKey).getOssReconciliationFile();
        if (StringUtils.isBlank(sourcePath) || StringUtils.isBlank(targetPath)) {
            return StatusResult.IGNORE;
        }
        return StatusResult.ACT;
    }

    @Override
    protected void innerHandle(OffLineTaskActivity taskActivity, String key) {
        String sourceKey = taskActivity.getTaskConfig().getSourceConfig().getKey();
        String targetKey = taskActivity.getTaskConfig().getTargetConfig().getKey();
        String sourcePath = taskActivity.getSourceContext(sourceKey).getOssReconciliationFile();
        String targetPath = taskActivity.getSourceContext(targetKey).getOssReconciliationFile();
        try {
            //下载oss对账文件
            Optional<File> sourceFile = ossUtil.downloadFileToLocal(sourcePath, taskActivity.getAbsoluteFilePath().append(sourceKey).append(OSS_COMPARE_FILE).toString());
            Optional<File> targetFile = ossUtil.downloadFileToLocal(targetPath, taskActivity.getAbsoluteFilePath().append(targetKey).append(OSS_COMPARE_FILE).toString());
            //对账
            OffLineActivityResult activityResult = taskActivity.verifyFileDetail(sourceFile.get().getAbsolutePath(), targetFile.get().getAbsolutePath());
            //对账结果落库
            taskActivity.setActivityResult(activityResult);
            activityResultRepository.save(taskActivity.getActivityResult());
            //清理对账文件
            fileUtil.deleteLocalFiles(Arrays.asList(sourceFile.get().getAbsolutePath(), targetFile.get().getAbsolutePath()));
        } catch (Exception e) {
            if (e instanceof StatusCodeException) {
                throw new StatusCodeException(((StatusCodeException) e).getStatusCode());
            }
            ErrorMsg msg = new ErrorMsg(taskActivity.getBizKey(), null, OffLineAction.DATA_COMPARE_DETAIL, "oss对账文件下载失败");
            throw new StatusCodeException(Status.error(JsonUtil.toJson(msg)));
        }
    }

    @Override
    protected void updateActivity(OffLineTaskActivity taskActivity, String key) {
        taskActivityRepository.toDataDone(taskActivity.getId(), taskActivity.getAction());
    }

    @Override
    protected void afterHandle(OffLineTaskActivity taskActivity, String key) {
        OffLineActionMqBody msgBody = new OffLineActionMqBody(taskActivity.getBizKey(), key);
        Message message = MessageBuilder.create().topic(Constants.ANUBIS_MQ_TASK_ACTIVITY_ACTION_TOPIC)
                .tag(DATA_DONE.getCode())
                .body(msgBody)
                .build();
        messageProducer.syncSend(message);
    }
}
