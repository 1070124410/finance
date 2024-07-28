package com.finance.anubis.controller;

import com.finance.anubis.request.TaskActivityReq;
import com.finance.anubis.response.OffLineTaskActivityRes;
import com.finance.anubis.service.OffLineTaskActivityService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/03/23 15:31
 * @Description
 **/
@RestController
public class OffLineTaskActivityController {

    private final OffLineTaskActivityService taskActivityService;

    public OffLineTaskActivityController(OffLineTaskActivityService taskActivityService) {
        this.taskActivityService = taskActivityService;
    }

    @Override
    public Result<OffLineTaskActivityRes> getById(Long taskActivityId) {
        if(null == taskActivityId){
            return Result.error(StatusCodeEnum.NullParam.getMessage());
        }
        return Result.success(taskActivityService.getById(taskActivityId));
    }

    @Override
    public Result<List<OffLineTaskActivityRes>> getByParams(TaskActivityReq taskActivityReq) {
        if(null == taskActivityReq){
            return Result.error(StatusCodeEnum.NullParam.getMessage());
        }
        return Result.success(taskActivityService.getByParams(taskActivityReq));
    }

    @Override
    public Result<List<OffLineTaskActivityRes>> getPageByParams(TaskActivityReq taskActivityReq) {
        if(null == taskActivityReq){
            return Result.error(StatusCodeEnum.NullParam.getMessage());
        }
        return Result.success(taskActivityService.getPageByParams(taskActivityReq));
    }
}
