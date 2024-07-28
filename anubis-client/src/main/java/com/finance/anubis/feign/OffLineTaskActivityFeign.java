package com.finance.anubis.feign;

import com.finance.anubis.constant.AnubisServiceConstant;
import com.finance.anubis.req.TaskActivityReq;
import com.finance.anubis.res.OffLineTaskActivityRes;
import com.guming.api.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 10:01
 * @Description
 **/
@FeignClient(name = AnubisServiceConstant.APPLICATION_NAME,
        decode404 = true,
        contextId = "offLineTaskActivityFeign")
public interface OffLineTaskActivityFeign {

    String PREFIX_PATH = "offLineTaskActivity";


    /**
     * 根据id查询任务活动
     *
     * @param taskActivityId
     * @return
     */
    @GetMapping(PREFIX_PATH + "/getById")
    Result<OffLineTaskActivityRes> getById(@RequestParam("taskActivityId") Long taskActivityId);


    /**
     * 根据可选参数查询任务活动
     *
     * @param taskActivityReq
     * @return
     */
    @GetMapping(PREFIX_PATH + "/getByParams")
    Result<List<OffLineTaskActivityRes>> getByParams(@RequestBody TaskActivityReq taskActivityReq);

    /**
     * 分页带参查询任务活动
     *
     * @param taskActivityReq
     * @return
     */
    @GetMapping(PREFIX_PATH + "/getPageByParams")
    Result<List<OffLineTaskActivityRes>> getPageByParams(@RequestBody TaskActivityReq taskActivityReq);
}
