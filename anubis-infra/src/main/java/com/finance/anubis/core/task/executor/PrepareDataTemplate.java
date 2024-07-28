package com.finance.anubis.core.task.executor;

import com.finance.anubis.core.constants.enums.OffLineResourceType;
import com.finance.anubis.core.task.model.OffLineTaskActivity;
import com.finance.anubis.core.util.ExternalSortUtil;
import com.finance.anubis.core.util.FileUtil;
import com.finance.anubis.exception.ErrorMsg;
import com.guming.api.json.JsonUtil;
import com.guming.api.pojo.Status;
import com.guming.common.exception.StatusCodeException;
import com.finance.anubis.core.util.OSSUtil;
import lombok.CustomLog;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;

import static com.finance.anubis.core.constants.Constants.LocalFile.RECONCILIATION_FILE;

/**
 * @Author yezhaoyang
 * @Date 2023/03/02 15:00
 * @Description
 **/
@Component
@CustomLog
public abstract class PrepareDataTemplate {

    @Getter
    protected final OffLineResourceType type;

    protected final OSSUtil ossUtil;

    protected final ExternalSortUtil fileSortUtil;

    protected final FileUtil fileUtil;


    public PrepareDataTemplate(OffLineResourceType type, OSSUtil ossUtil, ExternalSortUtil fileSortUtil, FileUtil fileUtil) {
        this.type = type;
        this.ossUtil = ossUtil;
        this.fileSortUtil = fileSortUtil;
        this.fileUtil = fileUtil;
    }

    public void handle(OffLineTaskActivity taskActivity, String key) {
        //准备对账数据：拉取、整理成小文件
        prepareData(taskActivity, key);
        //合并对账数据、计算总账
        mergeData(taskActivity, key);
        //保存总账和对账文件
        uploadData(taskActivity, key);
        //清理垃圾文件
        clearLocalData(taskActivity, key);
    }

    public abstract void prepareData(OffLineTaskActivity taskActivity, String key);


    public void mergeData(OffLineTaskActivity activity, String key) {
        String path = activity.getAbsoluteFilePath().append(key).append(RECONCILIATION_FILE).toString();
        Comparator comparator = activity.getComparator(key);
        String filePath = null;
        try {
            filePath = fileSortUtil.doMerge(activity.getSourceContext(key).getPathList(), path, comparator, activity, key);
            //对账文件为空,手动赋值总账
            if (activity.getSourceContext(key).getTotalAmount() == null) {
                activity.getSourceContext(key).setTotalAmount(new BigDecimal(BigInteger.ZERO));
            }
        } catch (Exception e) {
            String errorMsg = "mergeData fail,bizKey:" + activity.getBizKey() + " key:" + key;
            ErrorMsg msg = new ErrorMsg(activity.getBizKey(), key, activity.getAction(), errorMsg, e);
            throw new StatusCodeException(Status.error(JsonUtil.toJson(msg)));
        }

        fileUtil.renameLocalFile(filePath, path);
        activity.getSourceContext(key).setReconciliationFile(path);
    }

    public void uploadData(OffLineTaskActivity activity, String key) {
        try {
            //上传对账文件到oss
            File file = new File(activity.getSourceContext(key).getReconciliationFile());
            FileInputStream stream = new FileInputStream(file);
            String path = ossUtil.uploadFile(stream, activity.getBizKey(), key + RECONCILIATION_FILE);
            activity.getSourceContext(key).setOssReconciliationFile(path);
        } catch (Exception e) {
            String errorMsg = "uploadData fail,bizKey:" + activity.getBizKey() + " key:" + key;
            ErrorMsg msg = new ErrorMsg(activity.getBizKey(), key, activity.getAction(), errorMsg, e);
            throw new StatusCodeException(Status.error(JsonUtil.toJson(msg)));
        }

    }

    public void clearLocalData(OffLineTaskActivity taskActivity, String key) {
        try {
            //清理本地对账文件
            fileUtil.deleteLocalFiles(taskActivity.getSourceContext(key).getPathList());
            fileUtil.deleteLocalFiles(Collections.singletonList(taskActivity.getSourceContext(key).getReconciliationFile()));
        } catch (Exception e) {
            String errorMsg = "clearLocalData fail,bizKey:" + taskActivity.getBizKey() + " key:" + key;
            ErrorMsg msg = new ErrorMsg(taskActivity.getBizKey(), key, taskActivity.getAction(), errorMsg, e);
            throw new StatusCodeException(Status.error(JsonUtil.toJson(msg)));
        }
    }

}
