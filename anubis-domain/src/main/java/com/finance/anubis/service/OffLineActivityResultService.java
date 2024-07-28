package com.finance.anubis.service;

import com.finance.anubis.request.OffLineActivityResultReq;
import com.finance.anubis.response.OffLineActivityResultRes;

/**
 * @Author yezhaoyang
 * @Date 2023/01/18 17:12
 * @Description
 **/
public interface OffLineActivityResultService {

    /**
     * 查询对账结果(最新结果)
     *
     * @param activityResultReq
     * @return
     */
    OffLineActivityResultRes selectByParams(OffLineActivityResultReq activityResultReq);


}
