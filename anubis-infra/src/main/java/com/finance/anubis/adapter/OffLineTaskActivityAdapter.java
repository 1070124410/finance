package com.finance.anubis.adapter;

import com.finance.anubis.core.model.OffLineTaskActivity;
import com.finance.anubis.enums.OffLineAction;
import com.finance.anubis.request.TaskActivityReq;
import com.finance.anubis.response.OffLineTaskActivityRes;
import com.finance.anubis.utils.JsonUtil;

/**
 * @Author yezhaoyang
 * @Date 2023/03/23 15:57
 * @Description
 **/
public class OffLineTaskActivityAdapter {

    public static OffLineTaskActivity adapt2TaskActivity(TaskActivityReq req) {
        OffLineTaskActivity activity = new OffLineTaskActivity();
        activity.setId(req.getId());
        activity.setAction(OffLineAction.of(req.getAction()));
        return activity;
    }

    public static OffLineTaskActivityRes adapt2TaskActivityRes(OffLineTaskActivity activity) {
        OffLineTaskActivityRes res = new OffLineTaskActivityRes();
        res.setId(activity.getId());
        res.setAction(activity.getAction() == null ? null : activity.getAction().getCode());
        res.setContext(activity.getContext() == null ? null : JsonUtil.toJson(activity.getContext()));
        res.setTimes(activity.getTimes());
        res.setCreateTime(activity.getCreateTime());
        res.setUpdateTime(activity.getUpdateTime());
        res.setTaskConfigId(activity.getTaskConfig() == null ? null : activity.getTaskConfig().getId());
        return res;
    }


}
