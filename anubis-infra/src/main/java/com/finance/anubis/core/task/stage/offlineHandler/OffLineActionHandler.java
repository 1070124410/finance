package com.finance.anubis.core.task.stage.offlineHandler;

import com.finance.anubis.core.task.model.OffLineTaskActivity;
import com.finance.anubis.core.constants.enums.OffLineAction;
import com.finance.anubis.core.constants.enums.StatusResult;
import lombok.CustomLog;
import lombok.Getter;

/**
 * action 执行器
 */
@CustomLog
public abstract class OffLineActionHandler {

    @Getter
    private final OffLineAction action;

    public OffLineActionHandler(OffLineAction action) {
        this.action = action;
    }


    public void handle(OffLineTaskActivity taskActivity, String key) {
        StatusResult statusResult = checkStatus(taskActivity, key);
        switch (statusResult) {
            case IGNORE:
                break;
            case ONLY_PUSH:
                updateActivity(taskActivity,key);
                // 发送推进action的消息，如果报错，当前消息会失败，当前消息重试会驱动下一action
                afterHandle(taskActivity, key);
                break;
            case ACT:
                // 执行action内部操作
                innerHandle(taskActivity, key);
                //更新activity
                updateActivity(taskActivity, key);
                afterHandle(taskActivity, key);
                break;
            default:
                break;
        }
    }

    protected abstract StatusResult checkStatus(OffLineTaskActivity taskActivity, String key);


    protected abstract void innerHandle(OffLineTaskActivity taskActivity, String key);


    protected abstract void updateActivity(OffLineTaskActivity taskActivity, String key);


    protected abstract void afterHandle(OffLineTaskActivity taskActivity, String key);


}
