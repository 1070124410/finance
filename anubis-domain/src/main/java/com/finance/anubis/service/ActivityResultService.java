package com.finance.anubis.service;

import com.finance.anubis.request.ActivityResultReq;
import com.finance.anubis.response.ActivityResultRes;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/01/18 17:12
 * @Description
 **/
public interface ActivityResultService {

    /**
     * 按可选参数查询结果列表
     *
     * @param activityResultReq
     * @return
     */
    List<ActivityResultRes> selectByParams(ActivityResultReq activityResultReq);

    /**
     * 根据唯一信息查询最新结果
     * @param activityResultReq
     * @return
     */
    ActivityResultRes selectByUniqueInfo(ActivityResultReq activityResultReq);
}
