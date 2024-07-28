package com.finance.anubis.service;


import com.finance.anubis.request.TaskActivityReq;
import com.finance.anubis.response.OffLineTaskActivityRes;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/01/18 17:12
 * @Description
 **/
public interface OffLineTaskActivityService {

    /**
     * 根据id查询任务活动
     *
     * @param taskActivityId
     * @return
     */
    OffLineTaskActivityRes getById(Long taskActivityId);

    /**
     * 根据可选参数查询任务活动
     *
     * @param taskActivityReq
     * @return
     */
    List<OffLineTaskActivityRes> getByParams(TaskActivityReq taskActivityReq);


}
