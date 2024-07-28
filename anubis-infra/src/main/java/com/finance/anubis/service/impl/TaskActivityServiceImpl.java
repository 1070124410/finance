package com.finance.anubis.service.impl;

import com.finance.anubis.adapter.TaskActivityAdapter;
import com.finance.anubis.core.model.TaskActivity;
import com.finance.anubis.repository.TaskActivityRepository;
import com.finance.anubis.request.TaskActivityReq;
import com.finance.anubis.response.TaskActivityRes;
import com.finance.anubis.service.TaskActivityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yezhaoyang
 * @Date 2023/01/29 09:27
 * @Description
 **/
@Service
public class TaskActivityServiceImpl implements TaskActivityService {

    private final TaskActivityRepository taskActivityRepository;

    public TaskActivityServiceImpl(TaskActivityRepository taskActivityRepository) {
        this.taskActivityRepository = taskActivityRepository;
    }

    @Override
    public TaskActivityRes getById(Long taskActivityId) {
        if (null == taskActivityId) {
            return null;
        }
        TaskActivity taskActivity = taskActivityRepository.getById(taskActivityId);
        return TaskActivityAdapter.adapt2TaskActivityRes(taskActivity);
    }

    @Override
    public TaskActivityRes getByBizKey(String bizKey) {
        if (StringUtils.isBlank(bizKey)) {
            return null;
        }
        TaskActivity taskActivity = taskActivityRepository.getByBizKey(bizKey);
        return TaskActivityAdapter.adapt2TaskActivityRes(taskActivity);
    }

    @Override
    public List<TaskActivityRes> getByParams(TaskActivityReq taskActivityReq) {
        if (null == taskActivityReq) {
            return Collections.emptyList();
        }
        TaskActivity activity = TaskActivityAdapter.adapt2TaskActivity(taskActivityReq);

        return taskActivityRepository.getByParams(activity).stream().map(TaskActivityAdapter::adapt2TaskActivityRes).collect(Collectors.toList());
    }


}
