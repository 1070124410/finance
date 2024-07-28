package com.finance.anubis.controller;

import com.finance.anubis.req.TaskActivityReq;
import com.finance.anubis.res.TaskActivityRes;
import com.guming.api.pojo.Result;
import com.guming.common.web.annotation.JsonResponseBody;
import com.finance.anubis.feign.TaskActivityFeign;
import com.finance.anubis.service.TaskActivityService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 13:50
 * @Description
 **/
@RestController
@JsonResponseBody
public class TaskActivityController implements TaskActivityFeign {

    private final TaskActivityService taskActivityService;

    public TaskActivityController(TaskActivityService taskActivityService) {
        this.taskActivityService = taskActivityService;
    }

    @Override
    public Result<TaskActivityRes> getById(Long taskActivityId) {
        if (null == taskActivityId || taskActivityId <= 0) {
            return Result.error("empty or illegal param");
        }

        return Result.success(taskActivityService.getById(taskActivityId));
    }


    @Override
    public Result<List<TaskActivityRes>> getByParams(TaskActivityReq taskActivityReq) {
        if (null == taskActivityReq) {
            return Result.error("empty param");
        }

        return Result.success(taskActivityService.getByParams(taskActivityReq));
    }

    @Override
    public Result<List<TaskActivityRes>> getPageByParams(TaskActivityReq taskActivityReq) {
        if (null == taskActivityReq) {
            return Result.error("empty param");
        }

        return Result.success(taskActivityService.getPageByParams(taskActivityReq));
    }
}
