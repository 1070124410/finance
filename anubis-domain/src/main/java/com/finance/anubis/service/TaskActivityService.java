package com.finance.anubis.service;

import com.finance.anubis.request.TaskActivityReq;
import com.finance.anubis.response.TaskActivityRes;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/01/18 17:12
 * @Description
 **/
public interface TaskActivityService {

    /**
     * 根据id查询任务活动
     *
     * @param taskActivityId
     * @return
     */
    TaskActivityRes getById(Long taskActivityId);

    /**
     * 根据业务键查询任务活动
     *
     * @param bizKey
     * @return
     */
    TaskActivityRes getByBizKey(String bizKey);

    /**
     * 根据可选参数查询任务活动
     *
     * @param taskActivityReq
     * @return
     */
    List<TaskActivityRes> getByParams(TaskActivityReq taskActivityReq);


}
