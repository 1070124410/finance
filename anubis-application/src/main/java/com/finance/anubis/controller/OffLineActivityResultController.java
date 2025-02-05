package com.finance.anubis.controller;

import com.finance.anubis.service.OffLineActivityResultService;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author yezhaoyang
 * @Date 2023/03/23 15:31
 * @Description
 **/
@RestController
public class OffLineActivityResultController {

    private final OffLineActivityResultService resultService;

    public OffLineActivityResultController(OffLineActivityResultService resultService) {
        this.resultService = resultService;
    }

    @Override
    public Result<OffLineActivityResultRes> selectByParams(OffLineActivityResultReq activityResultReq) {
        if(activityResultReq==null){
            return Result.error(StatusCodeEnum.NullParam.getMessage());
        }
        return Result.success(resultService.selectByParams(activityResultReq));
    }

}
