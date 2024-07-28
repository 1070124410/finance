package com.finance.anubis.repository;

import com.finance.anubis.model.TaskActivityResult;

import java.util.List;

public interface ActivityResultRepository {
    /**
     * 保存执行结果
     *
     * @param taskActivityResult
     * @return
     */
    boolean save(TaskActivityResult taskActivityResult);

    /**
     * 更新执行结果(全量)
     *
     * @param taskActivityResult
     * @return
     */
    boolean update(TaskActivityResult taskActivityResult);


    /**
     * 按可选参数查询结果列表
     *
     * @param taskActivityResult
     * @return
     */
    List<TaskActivityResult> selectByParams(TaskActivityResult taskActivityResult);


    /**
     * 根据唯一信息查询最新结果
     * @param taskActivityResult
     */
    TaskActivityResult selectByUniqueInfo(TaskActivityResult taskActivityResult);
}
