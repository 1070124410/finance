package com.finance.anubis.feign;

import com.finance.anubis.constant.AnubisServiceConstant;
import com.finance.anubis.req.ActivityResultReq;
import com.finance.anubis.res.ActivityResultRes;
import com.guming.api.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 10:00
 * @Description
 **/
@FeignClient(name = AnubisServiceConstant.APPLICATION_NAME,
        decode404 = true,
        contextId = "activityResultFeign")
public interface ActivityResultFeign {

    String PREFIX_PATH = "activityResult";

    /**
     * 按可选参数查询结果列表
     *
     * @param activityResultReq
     * @return
     */
    @GetMapping(PREFIX_PATH + "/selectByParams")
    Result<List<ActivityResultRes>> selectByParams(@RequestBody ActivityResultReq activityResultReq);

    /**
     * 分页带参查询
     *
     * @param activityResultReq
     * @return
     */
    @GetMapping(PREFIX_PATH + "/selectPageByParams")
    Result<List<ActivityResultRes>> selectPageByParams(@RequestBody ActivityResultReq activityResultReq);

    /**
     * 根据唯一信息查询最新结果
     *
     * @param activityResultReq
     * @return
     */
    @GetMapping(PREFIX_PATH + "/selectByUniqueInfo")
    Result<ActivityResultRes> selectByUniqueInfo(@RequestBody ActivityResultReq activityResultReq);


}
