package com.finance.anubis.controller;

import com.finance.anubis.dto.OffLineDataReadyDTO;
import com.finance.anubis.service.OffLineTaskService;
import com.guming.api.pojo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


/**
 * @Author yezhaoyang
 * @Date 2023/03/07 19:07
 * @Description
 **/
@RestController
public class OffLineTaskController {

    private final OffLineTaskService taskService;


    public OffLineTaskController(OffLineTaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public Result<String> OffLineDataReady(OffLineDataReadyDTO dto) {
        if (StringUtils.isBlank(dto.getTaskName()) || StringUtils.isBlank(dto.getConfigKey())) {
            return Result.error(NullParam.getMessage());
        }
        try {
            LocalDateTime.parse(dto.getVerifyDate());
        } catch (Exception e) {
            return Result.error(NullParam.getMessage());
        }
        if (!taskService.dataReady(dto)) {
            return Result.error(TaskNotExist.getMessage());
        }

        return Result.success();
    }

}
