package com.finance.anubis.service.impl;

import com.finance.anubis.adapter.ActivityResultAdapter;
import com.finance.anubis.exception.Status;
import com.finance.anubis.exception.StatusCodeEnum;
import com.finance.anubis.exception.StatusCodeException;
import com.finance.anubis.model.TaskActivityResult;
import com.finance.anubis.repository.ActivityResultRepository;
import com.finance.anubis.request.ActivityResultReq;
import com.finance.anubis.response.ActivityResultRes;
import com.finance.anubis.service.ActivityResultService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author yezhaoyang
 * @Date 2023/01/18 17:24
 * @Description
 **/
@Service
public class ActivityResultServiceImpl implements ActivityResultService {

    private final ActivityResultRepository activityResultRepository;

    public ActivityResultServiceImpl(ActivityResultRepository activityResultRepository) {
        this.activityResultRepository = activityResultRepository;
    }


    @Override
    public List<ActivityResultRes> selectByParams(ActivityResultReq activityResultReq) {
        if (null == activityResultReq) {
            throw new StatusCodeException(Status.error(StatusCodeEnum.NullParam.getMessage()));
        }
        TaskActivityResult activityResult = ActivityResultAdapter.adapt2TaskActivityResult(activityResultReq);
        List<TaskActivityResult> taskActivityResults = activityResultRepository.selectByParams(activityResult);

        return taskActivityResults.stream().map(ActivityResultAdapter::adapt2ActionResultRes).collect(Collectors.toList());
    }

    @Override
    public ActivityResultRes selectByUniqueInfo(ActivityResultReq activityResultReq) {
        if (null == activityResultReq.getTaskActivityId() && StringUtils.isBlank(activityResultReq.getBizKey())) {
            throw new StatusCodeException(Status.error(StatusCodeEnum.NullParam.getMessage()));
        }
        if (StringUtils.isBlank(activityResultReq.getActionResult())) {
            activityResultReq.setActionResult(null);
        }
        TaskActivityResult activityResult = ActivityResultAdapter.adapt2TaskActivityResult(activityResultReq);
        TaskActivityResult taskActivityResult = activityResultRepository.selectByUniqueInfo(activityResult);

        return ActivityResultAdapter.adapt2ActionResultRes(taskActivityResult);
    }
}
