package com.finance.anubis.repository;

import com.finance.anubis.core.model.Task;
import com.finance.anubis.repository.entity.TaskEntity;

import java.util.List;

public interface TaskRepository {

    /**
     * 查询单个任务详情
     *
     * @param id
     * @return
     */
    Task selectById(Long id);


    /**
     * 根据任务条件查询任务
     *
     * @param taskEntity
     * @return
     */
    List<Task> selectTaskByParam(TaskEntity taskEntity);


    /**
     * 新增任务
     *
     * @param task
     * @return
     */
    Task add(Task task);


    /**
     * 更新任务
     *
     * @param task
     * @return
     */
    Task updateTask(Task task);


    /**
     * 开始任务
     *
     * @param taskId
     */
    boolean start(Long taskId);

    /**
     * 停止任务
     *
     * @param taskId
     */
    boolean stop(Long taskId);


    /**
     * 当前活跃任务列表
     * @return
     */
    List<Task> activeTaskList();


    /**
     * 根据任务名称查询
     * @return
     */
    Task selectByName(String taskName);
}
