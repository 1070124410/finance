package com.finance.anubis.feign;

import com.finance.anubis.constant.AnubisServiceConstant;
import com.finance.anubis.req.OffLineActivityResultReq;
import com.finance.anubis.res.OffLineActivityResultRes;
import com.guming.api.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 10:00
 * @Description
 **/
@FeignClient(name = AnubisServiceConstant.APPLICATION_NAME,
        decode404 = true,
        contextId = "offLineActivityResultFeign")
public interface OffLineActivityResultFeign {

    String PREFIX_PATH = "offLineActivityResult";

    /**
     * 按可选参数查询结果列表
     *
     * @param activityResultReq
     * @return
     */
    @GetMapping(PREFIX_PATH + "/selectByParams")
    Result<OffLineActivityResultRes> selectByParams(@RequestBody OffLineActivityResultReq activityResultReq);


}
