package com.finance.anubis.core.task.stage.offlineHandler;

import com.finance.anubis.core.model.OffLineTaskActivity;
import com.finance.anubis.core.util.DingTalkWebhookUtil;
import com.finance.anubis.core.util.FileUtil;
import com.finance.anubis.core.util.OSSUtil;
import com.finance.anubis.dto.OffLineTaskResultDTO;
import com.finance.anubis.enums.OffLineAction;
import com.finance.anubis.enums.OffLineActivityVerifyResult;
import com.finance.anubis.enums.StatusResult;
import com.finance.anubis.mq.MessageProducer;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.finance.anubis.utils.JsonUtil;
import lombok.CustomLog;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.finance.anubis.constants.Constants.LocalFile.OSS_COMPARE_FILE;
import static com.finance.anubis.constants.Constants.TASK_ACTIVITY_ACTION_TOPIC;
import static com.finance.anubis.constants.Constants.UNDERLINE_SPLIE;

/**
 * 快速失败执行器
 */
@Component
public class FailFastOffLineActionHandler extends OffLineActionHandler {

    private final OffLineTaskActivityRepository taskActivityRepository;

    private final FileUtil fileUtil;

    private final OSSUtil ossUtil;

    private final MessageProducer messageProducer;

    private final DingTalkWebhookUtil dingTalkWebhookUtil;

    public FailFastOffLineActionHandler(OffLineTaskActivityRepository taskActivityRepository, MessageProducer messageProducer,
                                        FileUtil fileUtil, OSSUtil ossUtil, DingTalkWebhookUtil dingTalkWebhookUtil) {
        super(OffLineAction.DATA_FAIL);
        this.taskActivityRepository = taskActivityRepository;
        this.fileUtil = fileUtil;
        this.ossUtil = ossUtil;
        this.messageProducer = messageProducer;
        this.dingTalkWebhookUtil = dingTalkWebhookUtil;
    }


    @Override
    protected StatusResult checkStatus(OffLineTaskActivity taskActivity, String key) {
        OffLineTaskActivity activity = taskActivityRepository.getByBizKey(taskActivity.getBizKey());
        if (activity.getAction().equals(OffLineAction.DATA_FAIL)) {
            return StatusResult.ACT;
        }
        return StatusResult.IGNORE;
    }

    @Override
    public void innerHandle(OffLineTaskActivity taskActivity, String key) {
//        String sourceKey = taskActivity.getOnLineTaskConfig().getSourceConfig().getKey();
//        String targetKey = taskActivity.getOnLineTaskConfig().getTargetConfig().getKey();
//        String taskName = taskActivity.getOnLineTaskConfig().getName();
        //清理本地文件
        fileUtil.deleteLocalFiles(waitCleanLocalFile(taskActivity));
        //清理OSS文件
//        ossUtil.delFile(taskName, taskActivity.generateFileName(sourceKey));
//        ossUtil.delFile(taskName, taskActivity.generateFileName(targetKey));
//        dingTalkWebhookUtil.sendAtAllMarkDown("对账任务快速失败完成", "对账数据清理完成,bizKey:" + taskActivity.getBizKey());
    }

    @Override
    public void updateActivity(OffLineTaskActivity taskActivity, String key) {

    }

    @Override
    public void afterHandle(OffLineTaskActivity taskActivity, String key) {
        OffLineTaskResultDTO dto = new OffLineTaskResultDTO();
        dto.setVerifyResult(OffLineActivityVerifyResult.ERROR);
        String tag = taskActivity.getTaskConfig().getName() + UNDERLINE_SPLIE + OffLineAction.DATA_FAIL.getCode();
        messageProducer.syncSend(TASK_ACTIVITY_ACTION_TOPIC, tag, JsonUtil.toJson(dto));
    }


    /**
     * 本地待清理文件
     *
     * @param taskActivity
     * @return
     */
    public static List<String> waitCleanLocalFile(OffLineTaskActivity taskActivity) {
        List<String> paths = new ArrayList<>();
        String sourceKey = taskActivity.getTaskConfig().getSourceConfig().getKey();
        String targetKey = taskActivity.getTaskConfig().getTargetConfig().getKey();
        paths.addAll(taskActivity.getSourceContext(sourceKey).getPathList());
        paths.add(taskActivity.getSourceContext(sourceKey).getReconciliationFile());
        paths.addAll(taskActivity.getSourceContext(targetKey).getPathList());
        paths.add(taskActivity.getSourceContext(targetKey).getReconciliationFile());
        paths.add(taskActivity.getAbsoluteFilePath().append(sourceKey).append(OSS_COMPARE_FILE).toString());
        paths.add(taskActivity.getAbsoluteFilePath().append(targetKey).append(OSS_COMPARE_FILE).toString());

        return paths;
    }
}
