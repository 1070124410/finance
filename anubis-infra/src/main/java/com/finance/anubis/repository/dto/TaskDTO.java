package com.finance.anubis.repository.dto;

import com.finance.anubis.core.model.Task;
import com.finance.anubis.enums.TaskType;
import com.finance.anubis.repository.entity.TaskConfigEntity;
import com.finance.anubis.repository.entity.TaskEntity;

public class TaskDTO {
    public static TaskEntity toEntity(Task task) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(task.getId());
        taskEntity.setTaskConfigId(task.getTaskConfig() == null ? null : task.getTaskConfig().getId());
        taskEntity.setStatus(task.getStatus());
        taskEntity.setType(task.getTaskType());
        return taskEntity;
    }

    public static Task toModel(TaskEntity taskEntity, TaskConfigEntity taskConfigEntity) {
        Task task = new Task();
        task.setStatus(taskEntity.getStatus());
        task.setId(taskEntity.getId());
        task.setTaskType(taskEntity.getType());
        //todo
        if (TaskType.ONLINE.equals(task.getTaskType())) {
            task.setTaskConfig(TaskConfigDTO.toModel(taskConfigEntity));
        } else if (TaskType.OFFLINE.equals(task.getTaskType())) {
            task.setTaskConfig(TaskConfigDTO.toOffLineModel(taskConfigEntity));
        }
        return task;
    }

}
