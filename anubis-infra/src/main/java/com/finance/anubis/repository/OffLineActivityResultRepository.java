package com.finance.anubis.repository;

import com.finance.anubis.core.task.model.OffLineActivityResult;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/03/09 13:53
 * @Description
 **/
public interface OffLineActivityResultRepository {

    /**
     * 保存执行结果
     *
     * @param taskActivityResult
     * @return
     */
    boolean save(OffLineActivityResult taskActivityResult);

    /**
     * 更新执行结果(全量)
     *
     * @param taskActivityResult
     * @return
     */
    boolean update(OffLineActivityResult taskActivityResult);

    /**
     * 根据唯一信息查询最新结果
     * @param taskActivityId
     */
    OffLineActivityResult selectTotalResult(Long taskActivityId);
    /**
     * 根据唯一信息查询最新结果
     * @param taskActivityId
     */
    OffLineActivityResult selectDetailResult(Long taskActivityId);

    /**
     * 根据唯一信息查询最新结果
     * @param result
     * @return
     */
    List<OffLineActivityResult> selectResultByParam(OffLineActivityResult result);
}
