package com.finance.anubis.controller;

import com.finance.anubis.res.TaskRes;
import com.guming.api.pojo.Result;
import com.guming.common.web.annotation.JsonResponseBody;
import com.finance.anubis.feign.TaskFeign;
import com.finance.anubis.req.TaskReq;
import com.finance.anubis.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.finance.anubis.exception.StatusCodeEnum.NullParam;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 13:50
 * @Description
 **/
@RestController
@JsonResponseBody
public class TaskController implements TaskFeign {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public Result<TaskRes> selectById(Long id) {
        if (null == id) {
            return Result.error(NullParam.getMessage());
        }

        return Result.success(taskService.selectById(id));
    }

    @Override
    public Result<List<TaskRes>> selectTaskPage(TaskReq taskReq) {
        if (null == taskReq) {
            return Result.error(NullParam.getMessage());
        }

        return Result.success(taskService.selectTaskPage(taskReq));
    }

    @Override
    public Result<List<TaskRes>> selectTaskByParam(TaskReq taskReq) {
        if (null == taskReq) {
            return Result.error(NullParam.getMessage());
        }

        return Result.success(taskService.selectTaskByParam(taskReq));
    }

    @Override
    public Result<TaskRes> add(TaskReq taskReq) {
        if (null == taskReq) {
            return Result.error(NullParam.getMessage());
        }
        return Result.success(taskService.add(taskReq));
    }


    @Override
    public Result<TaskRes> updateTask(TaskReq taskReq) {
        if (null == taskReq) {
            return Result.error(NullParam.getMessage());
        }
        return Result.success(taskService.updateTask(taskReq));
    }


    @Override
    public Result<String> startTask(Long taskId) {
        if (null == taskId) {
            return Result.error(NullParam.getMessage());
        }
        taskService.start(taskId);
        return Result.success();
    }

    @Override
    public Result<String> stopTask(Long taskId) {
        if (null == taskId) {
            return Result.error(NullParam.getMessage());
        }
        taskService.stop(taskId);
        return Result.success();
    }

    @Override
    public Result<String> compensateMessage(String messageBodyStr, Long taskId) {
        if (StringUtils.isBlank(messageBodyStr) || null == taskId) {
            return Result.error(NullParam.getMessage());
        }

        return Result.success(taskService.compensateMessage(messageBodyStr, taskId));
    }
}
