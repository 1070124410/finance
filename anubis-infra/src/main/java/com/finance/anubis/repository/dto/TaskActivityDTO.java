package com.finance.anubis.repository.dto;

import com.guming.api.json.JsonUtil;
import com.finance.anubis.core.config.OffLineTaskConfig;
import com.finance.anubis.core.config.OnLineTaskConfig;
import com.finance.anubis.core.context.ActivityContext;
import com.finance.anubis.core.context.OffLineActivityContext;
import com.finance.anubis.core.task.model.OffLineTaskActivity;
import com.finance.anubis.core.task.model.TaskActivity;
import com.finance.anubis.repository.entity.TaskActivityEntity;
import com.finance.anubis.repository.entity.TaskConfigEntity;

public class TaskActivityDTO {
    public static TaskActivityEntity toEntity(TaskActivity taskActivity) {
        TaskActivityEntity taskActivityEntity = new TaskActivityEntity();
        taskActivityEntity.setId(taskActivity.getId());
        taskActivityEntity.setBizKey(taskActivity.getBizKey());
        taskActivityEntity.setCreateTime(taskActivity.getCreateTime());
        taskActivityEntity.setUpdateTime(taskActivity.getUpdateTime());
        taskActivityEntity.setContext(JsonUtil.toJson(taskActivity.getContext()));
        taskActivityEntity.setAction(taskActivity.getAction() == null ? null : taskActivity.getAction());
        taskActivityEntity.setTaskConfigId(taskActivity.getOnLineTaskConfig() == null ? null : taskActivity.getOnLineTaskConfig().getId());
        taskActivityEntity.setTimes(taskActivity.getTimes());
        return taskActivityEntity;
    }

    public static TaskActivity toModel(TaskActivityEntity taskActivityEntity, TaskConfigEntity taskConfigEntity) {
        TaskActivity taskActivity = new TaskActivity();
        String context = taskActivityEntity.getContext();
        taskActivity.setContext(JsonUtil.of(context, ActivityContext.class));
        taskActivity.setAction(taskActivityEntity.getAction() == null ? null : taskActivityEntity.getAction());
        taskActivity.setId(taskActivityEntity.getId());
        taskActivity.setTimes(taskActivityEntity.getTimes());
        taskActivity.setCreateTime(taskActivityEntity.getCreateTime());
        taskActivity.setUpdateTime(taskActivityEntity.getUpdateTime());
        OnLineTaskConfig onLineTaskConfig = TaskConfigDTO.toModel(taskConfigEntity);
        taskActivity.setOnLineTaskConfig(onLineTaskConfig);
        return taskActivity;
    }

    public static TaskActivityEntity toOffLineEntity(OffLineTaskActivity taskActivity) {
        TaskActivityEntity taskActivityEntity = new TaskActivityEntity();
        taskActivityEntity.setId(taskActivity.getId());
        taskActivityEntity.setBizKey(taskActivity.getBizKey());
        taskActivityEntity.setCreateTime(taskActivity.getCreateTime());
        taskActivityEntity.setUpdateTime(taskActivity.getUpdateTime());
        taskActivityEntity.setContext(JsonUtil.toJson(taskActivity.getContext()));
        taskActivityEntity.setOffLineAction(taskActivity.getAction() == null ? null : taskActivity.getAction());
        taskActivityEntity.setTaskConfigId(taskActivity.getTaskConfig() == null ? null : taskActivity.getTaskConfig().getId());
        taskActivityEntity.setTimes(taskActivity.getTimes());
        return taskActivityEntity;
    }

    public static OffLineTaskActivity toOffLineModel(TaskActivityEntity taskActivityEntity, TaskConfigEntity taskConfigEntity) {
        OffLineTaskActivity taskActivity = new OffLineTaskActivity();
        String context = taskActivityEntity.getContext();
        taskActivity.setContext(JsonUtil.of(context, OffLineActivityContext.class));
        taskActivity.setAction(taskActivityEntity.getOffLineAction() == null ? null : taskActivityEntity.getOffLineAction());
        taskActivity.setId(taskActivityEntity.getId());
        taskActivity.setTimes(taskActivityEntity.getTimes());
        taskActivity.setCreateTime(taskActivityEntity.getCreateTime());
        taskActivity.setUpdateTime(taskActivityEntity.getUpdateTime());
        OffLineTaskConfig taskConfig = TaskConfigDTO.toOffLineModel(taskConfigEntity);
        taskActivity.setTaskConfig(taskConfig);
        return taskActivity;
    }
}
