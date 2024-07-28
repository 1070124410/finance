package com.finance.anubis.repository.dto;

import com.finance.anubis.model.TaskActivityResult;
import com.finance.anubis.repository.entity.TaskActivityResultEntity;
import com.finance.anubis.utils.JsonUtil;

/**
 * @Author yezhaoyang
 * @Date 2023/01/13 10:02
 * @Description
 **/
public class TaskActivityResultDTO {

    public static TaskActivityResultEntity toEntity(TaskActivityResult taskActivityResult) {
        TaskActivityResultEntity taskActivityResultEntity = new TaskActivityResultEntity();
        taskActivityResultEntity.setId(taskActivityResult.getId());
        taskActivityResultEntity.setTaskActivityId(taskActivityResult.getTaskActivityId());
        taskActivityResultEntity.setCompareSourceData(JsonUtil.toJson(taskActivityResult.getCompareSourceData()));
        taskActivityResultEntity.setCompareTargetData(JsonUtil.toJson(taskActivityResult.getCompareTargetData()));
        taskActivityResultEntity.setCompareKeys(JsonUtil.toJson(taskActivityResult.getCompareKeys()));
        taskActivityResultEntity.setVarianceKeys(JsonUtil.toJson(taskActivityResult.getVarianceKeys()));
        taskActivityResultEntity.setActionResult(taskActivityResult.getActionResult() == null ? null : taskActivityResult.getActionResult());
        taskActivityResultEntity.setBizKey(taskActivityResult.getBizKey());
        taskActivityResultEntity.setTimes(taskActivityResult.getTimes());
        taskActivityResultEntity.setCreateTime(taskActivityResult.getCreateTime());
        taskActivityResultEntity.setUpdateTime(taskActivityResult.getUpdateTime());
        return taskActivityResultEntity;
    }

    public static TaskActivityResult toModel(TaskActivityResultEntity taskActivityResultEntity) {
        TaskActivityResult taskActivityResult = new TaskActivityResult();
        taskActivityResult.setId(taskActivityResultEntity.getId());
        taskActivityResult.setTaskActivityId(taskActivityResultEntity.getTaskActivityId());
        taskActivityResult.setCompareSourceData(JsonUtil.ofMap(taskActivityResultEntity.getCompareSourceData(), String.class, Object.class));
        taskActivityResult.setCompareTargetData(JsonUtil.ofMap(taskActivityResultEntity.getCompareTargetData(), String.class, Object.class));
        taskActivityResult.setCompareKeys(JsonUtil.ofList(taskActivityResultEntity.getCompareKeys(), String.class));
        taskActivityResult.setVarianceKeys(JsonUtil.ofList(taskActivityResultEntity.getVarianceKeys(), String.class));
        taskActivityResult.setActionResult(taskActivityResultEntity.getActionResult() == null ? null : taskActivityResultEntity.getActionResult());
        taskActivityResult.setCreateTime(taskActivityResultEntity.getCreateTime());
        taskActivityResult.setUpdateTime(taskActivityResultEntity.getUpdateTime());
        taskActivityResult.setBizKey(taskActivityResultEntity.getBizKey());
        taskActivityResult.setTimes(taskActivityResultEntity.getTimes());

        return taskActivityResult;
    }
}
