package com.finance.anubis.controller;

import com.finance.anubis.res.ActivityResultRes;
import com.guming.api.pojo.Result;
import com.guming.common.web.annotation.JsonResponseBody;
import com.finance.anubis.feign.ActivityResultFeign;
import com.finance.anubis.req.ActivityResultReq;
import com.finance.anubis.service.ActivityResultService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 13:50
 * @Description
 **/
@RestController
@JsonResponseBody
public class ActivityResultController implements ActivityResultFeign {

    private final ActivityResultService activityResultService;

    public ActivityResultController(ActivityResultService activityResultService) {
        this.activityResultService = activityResultService;
    }

    @Override
    public Result<List<ActivityResultRes>> selectByParams(ActivityResultReq activityResultReq) {
        if (null == activityResultReq) {
            return Result.error("empty param");
        }

        return Result.success(activityResultService.selectByParams(activityResultReq));
    }

    @Override
    public Result<List<ActivityResultRes>> selectPageByParams(ActivityResultReq activityResultReq) {
        if (null == activityResultReq) {
            return Result.error("empty param");
        }

        return Result.success(activityResultService.selectPageByParams(activityResultReq));
    }

    @Override
    public Result<ActivityResultRes> selectByUniqueInfo(ActivityResultReq activityResultReq) {
        if (null == activityResultReq.getTaskActivityId() && StringUtils.isBlank(activityResultReq.getBizKey())) {
            return Result.error("empty param");
        }

        return Result.success(activityResultService.selectByUniqueInfo(activityResultReq));
    }

}
