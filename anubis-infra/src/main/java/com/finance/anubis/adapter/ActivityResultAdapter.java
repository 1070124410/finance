package com.finance.anubis.adapter;

import com.finance.anubis.model.TaskActivityResult;
import com.finance.anubis.request.ActivityResultReq;
import com.finance.anubis.response.ActivityResultRes;
import com.finance.anubis.utils.JsonUtil;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 15:04
 * @Description
 **/
public class ActivityResultAdapter {

    public static TaskActivityResult adapt2TaskActivityResult(ActivityResultReq activityResultReq) {
        String jsonString = JsonUtil.toJson(activityResultReq);

        return JsonUtil.string2Obj(jsonString, TaskActivityResult.class);
    }

    public static ActivityResultRes adapt2ActionResultRes(TaskActivityResult taskActivityResult) {
        ActivityResultRes resultRes = new ActivityResultRes();
        resultRes.setId(taskActivityResult.getId());
        resultRes.setTaskActivityId(taskActivityResult.getTaskActivityId());
        resultRes.setCreateTime(taskActivityResult.getCreateTime());
        resultRes.setUpdateTime(taskActivityResult.getUpdateTime());
        resultRes.setBizKey(taskActivityResult.getBizKey());
        resultRes.setCompareSourceData(taskActivityResult.getCompareSourceData().toString());
        resultRes.setCompareTargetData(taskActivityResult.getCompareTargetData().toString());
        resultRes.setCompareKeys(taskActivityResult.getCompareKeys().toString());
        resultRes.setVarianceKeys(taskActivityResult.getVarianceKeys().toString());
        resultRes.setActionResult(taskActivityResult.getActionResult() == null ? null : taskActivityResult.getActionResult().getCode());
        resultRes.setTimes(taskActivityResult.getTimes());

        return resultRes;
    }
}
