package com.finance.anubis.service.impl;

import com.finance.anubis.adapter.OffLineTaskActivityAdapter;
import com.finance.anubis.core.model.OffLineTaskActivity;
import com.finance.anubis.repository.OffLineTaskActivityRepository;
import com.finance.anubis.request.TaskActivityReq;
import com.finance.anubis.response.OffLineTaskActivityRes;
import com.finance.anubis.service.OffLineTaskActivityService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yezhaoyang
 * @Date 2023/01/18 17:12
 * @Description
 **/
@Service
public class OffLineTaskActivityServiceImpl implements OffLineTaskActivityService {

    private final OffLineTaskActivityRepository activityRepository;

    public OffLineTaskActivityServiceImpl(OffLineTaskActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public OffLineTaskActivityRes getById(Long taskActivityId){
        if (null == taskActivityId) {
            return null;
        }
        OffLineTaskActivity taskActivity = activityRepository.getById(taskActivityId);
        return OffLineTaskActivityAdapter.adapt2TaskActivityRes(taskActivity);
    }

    @Override
    public List<OffLineTaskActivityRes> getByParams(TaskActivityReq taskActivityReq){
        if (null == taskActivityReq) {
            return Collections.emptyList();
        }
        OffLineTaskActivity taskActivity = OffLineTaskActivityAdapter.adapt2TaskActivity(taskActivityReq);

        return activityRepository.getByParams(taskActivity).stream().map(OffLineTaskActivityAdapter::adapt2TaskActivityRes).collect(Collectors.toList());
    }

}
