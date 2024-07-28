package com.finance.anubis.adapter;

import com.finance.anubis.config.TaskConfig;
import com.finance.anubis.core.model.Task;
import com.finance.anubis.enums.TaskStatus;
import com.finance.anubis.enums.TaskType;
import com.finance.anubis.repository.entity.TaskEntity;
import com.finance.anubis.request.TaskReq;
import com.finance.anubis.response.TaskRes;
import com.finance.anubis.utils.JsonUtil;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 15:03
 * @Description
 **/
public class TaskAdapter {

    public static Task adapt2Task(TaskReq taskReq) {
        Task task = new Task();
        String type = taskReq.getType();
        task.setTaskType(TaskType.of(type));
        if (taskReq.getConfig() != null) {
            String configJson = JsonUtil.toJson(taskReq.getConfig());
            task.setTaskConfig(JsonUtil.string2Obj(configJson, TaskConfig.class));
        }
        task.setStatus(TaskStatus.of(taskReq.getStatus()));
        task.setId(taskReq.getId());
        task.setCreateTime(taskReq.getCreateTime());
        task.setUpdateTime(taskReq.getUpdateTime());

        return task;
    }

    public static TaskRes adapt2TaskRes(Task task) {
        TaskRes res = new TaskRes();
        res.setId(task.getId());
        res.setStatus(task.getStatus().getCode());
        res.setType(task.getTaskType().getCode());
        res.setCreateTime(task.getCreateTime());
        res.setUpdateTime(task.getUpdateTime());
        res.setConfig(JsonUtil.toJson(task.getTaskConfig()));

        return res;
    }

    public static TaskEntity adapt2TaskEntity(TaskReq taskReq) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskReq.getId());
        taskEntity.setType(TaskType.of(taskReq.getType()));
        taskEntity.setCreateTime(taskReq.getCreateTime());
        taskEntity.setUpdateTime(taskReq.getUpdateTime());
        taskEntity.setTaskConfigId(taskReq.getConfig() == null ? null : taskReq.getConfig().getId());
        taskEntity.setStatus(TaskStatus.of(taskReq.getStatus()));
        return taskEntity;
    }
}
