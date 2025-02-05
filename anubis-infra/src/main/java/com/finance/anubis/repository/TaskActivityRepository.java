package com.finance.anubis.repository;

import com.finance.anubis.core.model.TaskActivity;
import com.finance.anubis.enums.Action;

import java.util.List;

public interface TaskActivityRepository {
    /**
     * 保存任务活动
     *
     * @param taskActivity
     * @return
     */
    boolean save(TaskActivity taskActivity);

    /**
     * 更新任务活动(全量)
     *
     * @param taskActivity
     * @return
     */
    boolean update(TaskActivity taskActivity);

    /**
     * 更新任务状态(Back使用)
     * @param id
     * @param action
     * @return
     */
    boolean updateAction(Long id, Action action);

    /**
     * 根据id查询任务活动
     *
     * @param taskActivityId
     * @return
     */
    TaskActivity getById(Long taskActivityId);

    /**
     * 根据业务键查询任务活动
     *
     * @param bizKey
     * @return
     */
    TaskActivity getByBizKey(String bizKey);

    /**
     * 根据可选参数查询任务活动
     *
     * @param activity
     * @return
     */
    List<TaskActivity> getByParams(TaskActivity activity);


}
