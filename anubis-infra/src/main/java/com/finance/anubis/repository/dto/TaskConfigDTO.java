package com.finance.anubis.repository.dto;

import cn.hutool.json.JSONUtil;
import com.finance.anubis.core.config.*;
import com.finance.anubis.core.constants.enums.TaskType;
import com.finance.anubis.repository.entity.TaskConfigEntity;
import com.guming.api.json.JsonUtil;

import java.util.Collections;

//todo 新增type字段去除类型判断
public class TaskConfigDTO {

    public static TaskConfigEntity toEntity(TaskConfig taskConfig) {
        TaskConfigEntity taskConfigEntity = new TaskConfigEntity();
        taskConfigEntity.setId(taskConfig.getId());
        taskConfigEntity.setUniqueKeys(taskConfig.getUniqueKeys());
        taskConfigEntity.setName(taskConfig.getName());
        taskConfigEntity.setVersion(taskConfig.getVersion());
        if (TaskType.ONLINE.equals(TaskType.of(taskConfig.getType()))) {
            OnLineTaskConfig config= (OnLineTaskConfig) taskConfig;
            taskConfigEntity.setSourceConfig(config.getSourceConfig() == null ? null : JsonUtil.of(config.getSourceConfig()));
            taskConfigEntity.setTargetConfig(config.getTargetConfigs() == null ? null : JsonUtil.of(config.getTargetConfigs()));
            taskConfigEntity.setCompareKeys(config.getCompareKeys());
            taskConfigEntity.setDelay(config.getDelay());
            taskConfigEntity.setErrorThreshold(0);
            taskConfigEntity.setDetailSwitch(false);
            taskConfigEntity.setRetryTime(0);
        } else if (TaskType.OFFLINE.equals(TaskType.of(taskConfig.getType()))) {
            OffLineTaskConfig config= (OffLineTaskConfig) taskConfig;
            taskConfigEntity.setSourceConfig(config.getSourceConfig() == null ? null : JsonUtil.of(config.getSourceConfig()));
            taskConfigEntity.setTargetConfig(config.getTargetConfig() == null ? null : JsonUtil.of(config.getTargetConfig()));
            taskConfigEntity.setErrorThreshold(config.getErrorThreshold());
            taskConfigEntity.setDetailSwitch(config.getDetailSwitch());
            taskConfigEntity.setRetryTime(config.getRetryTime());
            taskConfigEntity.setCompareKeys(Collections.emptyList());
            taskConfigEntity.setDelay(0);
        }
        return taskConfigEntity;
    }

    public static TaskConfigEntity toEntity(OnLineTaskConfig onLineTaskConfig) {
        TaskConfigEntity taskConfigEntity = new TaskConfigEntity();
        taskConfigEntity.setId(onLineTaskConfig.getId());
        taskConfigEntity.setSourceConfig(onLineTaskConfig.getSourceConfig() == null ? null : JsonUtil.of(onLineTaskConfig.getSourceConfig()));
        taskConfigEntity.setTargetConfig(onLineTaskConfig.getTargetConfigs() == null ? null : JsonUtil.of(onLineTaskConfig.getTargetConfigs()));
        taskConfigEntity.setCompareKeys(onLineTaskConfig.getCompareKeys());
        taskConfigEntity.setDelay(onLineTaskConfig.getDelay());
        taskConfigEntity.setErrorThreshold(0);
        taskConfigEntity.setDetailSwitch(false);
        taskConfigEntity.setRetryTime(0);
        taskConfigEntity.setUniqueKeys(onLineTaskConfig.getUniqueKeys());
        taskConfigEntity.setName(onLineTaskConfig.getName());
        taskConfigEntity.setVersion(onLineTaskConfig.getVersion());
        return taskConfigEntity;
    }

    public static OnLineTaskConfig toModel(TaskConfigEntity taskConfigEntity) {
        OnLineTaskConfig onLineTaskConfig = new OnLineTaskConfig();
        String sourceConfig = taskConfigEntity.getSourceConfig();
        Object resourceTypeObj = JSONUtil.parse(sourceConfig).getByPath("resourceType");
        if (resourceTypeObj == null) {
            throw new RuntimeException("配置错误");
        }
        onLineTaskConfig.setSourceConfig(JsonUtil.of(sourceConfig, MessageResourceConfig.class));
        onLineTaskConfig.setTargetConfigs(JsonUtil.ofList(taskConfigEntity.getTargetConfig(), URLResourceConfig.class));
        onLineTaskConfig.setCompareKeys(taskConfigEntity.getCompareKeys());
        onLineTaskConfig.setDelay(taskConfigEntity.getDelay());
        onLineTaskConfig.setUniqueKeys(taskConfigEntity.getUniqueKeys());
        onLineTaskConfig.setName(taskConfigEntity.getName());
        onLineTaskConfig.setId(taskConfigEntity.getId());
        onLineTaskConfig.setVersion(taskConfigEntity.getVersion());
        return onLineTaskConfig;
    }

    public static OffLineTaskConfig toOffLineModel(TaskConfigEntity taskConfigEntity) {
        OffLineTaskConfig taskConfig = new OffLineTaskConfig();
        String sourceConfig = taskConfigEntity.getSourceConfig();
        String targetConfig = taskConfigEntity.getTargetConfig();
        Object sourceResourceType = JSONUtil.parse(sourceConfig).getByPath("resourceType");
        Object targetResourceType = JSONUtil.parse(targetConfig).getByPath("resourceType");
        if (sourceResourceType == null || targetResourceType == null) {
            throw new RuntimeException("配置错误");
        }
        taskConfig.setSourceConfig(JsonUtil.of(sourceConfig, OffLineResourceConfig.class));
        taskConfig.setTargetConfig(JsonUtil.of(targetConfig, OffLineResourceConfig.class));
        taskConfig.setDetailSwitch(taskConfigEntity.getDetailSwitch());
        taskConfig.setErrorThreshold(taskConfigEntity.getErrorThreshold());
        taskConfig.setRetryTime(taskConfigEntity.getRetryTime() == null ? 0 : taskConfigEntity.getRetryTime());
        taskConfig.setUniqueKeys(taskConfigEntity.getUniqueKeys());
        taskConfig.setName(taskConfigEntity.getName());
        taskConfig.setId(taskConfigEntity.getId());
        taskConfig.setVersion(taskConfigEntity.getVersion());
        return taskConfig;
    }

    public static TaskConfigEntity toEntity(OffLineTaskConfig taskConfig) {
        TaskConfigEntity taskConfigEntity = new TaskConfigEntity();
        taskConfigEntity.setId(taskConfig.getId());
        taskConfigEntity.setSourceConfig(taskConfig.getSourceConfig() == null ? null : JsonUtil.of(taskConfig.getSourceConfig()));
        taskConfigEntity.setTargetConfig(taskConfig.getTargetConfig() == null ? null : JsonUtil.of(taskConfig.getTargetConfig()));
        taskConfigEntity.setErrorThreshold(taskConfig.getErrorThreshold());
        taskConfigEntity.setDetailSwitch(taskConfig.getDetailSwitch());
        taskConfigEntity.setRetryTime(taskConfig.getRetryTime());
        taskConfigEntity.setUniqueKeys(taskConfig.getUniqueKeys());
        taskConfigEntity.setName(taskConfig.getName());
        taskConfigEntity.setVersion(taskConfig.getVersion());
        taskConfigEntity.setCompareKeys(Collections.emptyList());
        taskConfigEntity.setDelay(0);
        return taskConfigEntity;
    }
}
