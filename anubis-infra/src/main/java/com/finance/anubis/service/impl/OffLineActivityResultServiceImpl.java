package com.finance.anubis.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.finance.anubis.repository.OffLineActivityResultRepository;
import com.finance.anubis.res.OffLineActivityResultRes;
import com.finance.anubis.adapter.OffLineActivityResultAdapter;
import com.finance.anubis.core.constants.enums.OffLineActivityResultType;
import com.finance.anubis.core.task.model.OffLineActivityResult;
import com.finance.anubis.req.OffLineActivityResultReq;
import com.finance.anubis.service.OffLineActivityResultService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.finance.anubis.core.constants.enums.OffLineActivityResultType.DETAIL;
import static com.finance.anubis.core.constants.enums.OffLineActivityResultType.TOTAL;

/**
 * @Author yezhaoyang
 * @Date 2023/03/23 15:36
 * @Description
 **/
@Service
public class OffLineActivityResultServiceImpl implements OffLineActivityResultService {

    private final OffLineActivityResultRepository resultRepository;

    public OffLineActivityResultServiceImpl(OffLineActivityResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }


    @Override
    public OffLineActivityResultRes selectByParams(OffLineActivityResultReq activityResultReq) {
        if (activityResultReq == null) {
            return null;
        }
        OffLineActivityResult result = OffLineActivityResultAdapter.adapt2OffLineActivityResult(activityResultReq);
        List<OffLineActivityResult> resultList = resultRepository.selectResultByParam(result);
        if (CollUtil.isEmpty(resultList)) {
            return null;
        }
        Map<OffLineActivityResultType, List<OffLineActivityResult>> map = resultList.stream().collect(Collectors.groupingBy(OffLineActivityResult::getResultType));
        OffLineActivityResult total = map.containsKey(TOTAL) ? map.get(TOTAL).get(0) : null;
        OffLineActivityResult detail = map.containsKey(DETAIL) ? map.get(DETAIL).get(0) : null;


        return OffLineActivityResultAdapter.adapt2OffLineActivityResultRes(total, detail);
    }

}
