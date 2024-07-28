package com.finance.anubis.adapter;

import com.finance.anubis.req.TaskActivityReq;
import com.finance.anubis.res.TaskActivityRes;
import com.guming.api.json.JsonUtil;
import com.finance.anubis.core.constants.enums.Action;
import com.finance.anubis.core.task.model.TaskActivity;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 15:04
 * @Description
 **/
public class TaskActivityAdapter {

    public static TaskActivity adapt2TaskActivity(TaskActivityReq taskActivityReq) {
        TaskActivity activity = new TaskActivity();
        activity.setId(taskActivityReq.getId());
        activity.setAction(Action.of(taskActivityReq.getAction()));
        return activity;
    }

    public static TaskActivityRes adapt2TaskActivityRes(TaskActivity taskActivity) {
        TaskActivityRes res = new TaskActivityRes();
        res.setId(taskActivity.getId());
        res.setAction(taskActivity.getAction() == null ? null : taskActivity.getAction().getCode());
        res.setTimes(taskActivity.getTimes());
        res.setCreateTime(taskActivity.getCreateTime());
        res.setUpdateTime(taskActivity.getUpdateTime());
        res.setContext(taskActivity.getContext() == null ? null : JsonUtil.toJson(taskActivity.getContext()));
        res.setTaskConfigId(taskActivity.getOnLineTaskConfig() == null ? null : taskActivity.getOnLineTaskConfig().getId());
        return res;
    }


}
