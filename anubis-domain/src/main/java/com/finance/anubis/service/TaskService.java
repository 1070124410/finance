package com.finance.anubis.service;

import com.finance.anubis.req.TaskReq;
import com.finance.anubis.res.TaskRes;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/01/18 14:03
 * @Description
 **/
public interface TaskService {

    /**
     * 查询单个任务详情
     *
     * @param id
     * @return
     */
    TaskRes selectById(Long id);

    /**
     * 分页查询任务列表
     *
     * @param task
     * @return
     */
    List<TaskRes> selectTaskPage(TaskReq task);

    /**
     * 根据任务条件查询任务
     *
     * @param taskReq
     * @return
     */
    List<TaskRes> selectTaskByParam(TaskReq taskReq);


    /**
     * 新增任务
     *
     * @param task
     * @return
     */
    TaskRes add(TaskReq task);

    /**
     * 更新任务
     *
     * @param taskReq
     * @return
     */
    TaskRes updateTask(TaskReq taskReq);


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
     * 补偿mq
     * @param messageBodyStr
     * @param taskId
     */
    String compensateMessage(String messageBodyStr, Long taskId);
}
