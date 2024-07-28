package com.finance.anubis.repository;

import com.finance.anubis.core.context.OffLineActivityContext;
import com.finance.anubis.core.model.OffLineTaskActivity;
import com.finance.anubis.enums.OffLineAction;

import java.util.List;

public interface OffLineTaskActivityRepository {
    /**
     * 保存任务活动
     *
     * @param taskActivity
     * @return
     */
    boolean save(OffLineTaskActivity taskActivity);

    /**
     * 更新任务活动(全量)
     *
     * @param taskActivity
     * @return
     */
    boolean update(OffLineTaskActivity taskActivity);

    /**
     * 根据业务键查询任务活动
     *
     * @param bizKey
     * @return
     */
    OffLineTaskActivity getByBizKey(String bizKey);

    boolean toDataFetch(Long id, OffLineActivityContext context, OffLineAction action);

    boolean toFail(Long id, OffLineAction action);

    boolean toDataCompareDetail(Long id, OffLineAction action);

    boolean toDataCompareTotal(Long id, OffLineAction action);

    boolean toDataDone(Long id, OffLineAction action);

    /**
     * 更新
     * @param id
     * @return
     */
    boolean updateTimes(Long id);

    boolean updateContext(Long id, OffLineActivityContext context);

    /**
     * 查询今日活跃的的对账任务
     * @return
     */
    List<OffLineTaskActivity> selectActiveList();

    /**
     * 对账完成后重置context内容
     * @param id
     * @param context
     */
    void resetContext(Long id, OffLineActivityContext context);

    /**
     * 更新任务状态(Back使用)
     * @param id
     * @param action
     * @return
     */
    boolean updateAction(Long id, OffLineAction action);

    /**
     * 根据id查询任务
     * @param taskActivityId
     * @return
     */
    OffLineTaskActivity getById(Long taskActivityId);

    /**
     * 根据参数批量查询
     * @param activity
     * @return
     */
    List<OffLineTaskActivity> getByParams(OffLineTaskActivity activity);

}
