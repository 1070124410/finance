package com.finance.anubis.adapter;

import com.finance.anubis.core.constants.enums.ActionResult;
import com.finance.anubis.core.task.model.OffLineActivityResult;
import com.finance.anubis.dto.OffLineTaskDetailResultDTO;
import com.finance.anubis.dto.OffLineTaskTotalResultDTO;
import com.finance.anubis.enums.OffLineActivityResultType;
import com.finance.anubis.enums.OffLineActivityVerifyResult;
import com.finance.anubis.req.OffLineActivityResultReq;
import com.finance.anubis.res.OffLineActivityResultRes;
import com.guming.api.json.JsonUtil;

/**
 * @Author yezhaoyang
 * @Date 2023/03/09 16:04
 * @Description
 **/
public class OffLineActivityResultAdapter {

    public static OffLineActivityResult adapt2OffLineActivityResult(OffLineActivityResultReq req) {
        OffLineActivityResult result = new OffLineActivityResult();
        result.setId(req.getId());
        result.setTaskActivityId(req.getTaskActivityId());
        result.setResultType(com.finance.anubis.core.constants.enums.OffLineActivityResultType.of(req.getResultType()));
        result.setVerifyResult(ActionResult.of(req.getVerifyResult()));
        result.setBizKey(req.getBizKey());
        return result;
    }

    public static OffLineActivityResultRes adapt2OffLineActivityResultRes(OffLineActivityResult totalResult, OffLineActivityResult detailResult) {
        OffLineActivityResultRes res = new OffLineActivityResultRes();
        res.setId(totalResult.getId() == null ? detailResult.getId() : totalResult.getId());
        res.setTaskActivityId(totalResult.getTaskActivityId() == null ? detailResult.getTaskActivityId() : totalResult.getTaskActivityId());
        res.setBizKey(totalResult.getBizKey() == null ? detailResult.getBizKey() : totalResult.getBizKey());
        res.setCreateTime(totalResult.getCreateTime() == null ? detailResult.getCreateTime() : totalResult.getCreateTime());
        res.setUpdateTime(totalResult.getUpdateTime() == null ? detailResult.getUpdateTime() : totalResult.getUpdateTime());
        res.setTimes(totalResult.getTimes() == null ? detailResult.getTimes() : totalResult.getTimes());
        res.setTotalResult(totalResult == null ? null : JsonUtil.toJson(totalResult));
        res.setDetailResult(detailResult == null ? null : JsonUtil.toJson(detailResult));
        return res;
    }

    public static OffLineTaskTotalResultDTO adapt2OffLineTaskTotalResultDTO(OffLineActivityResult result) {
        OffLineTaskTotalResultDTO dto = new OffLineTaskTotalResultDTO();
        OffLineActivityResult.TotalResultInfo resultInfo = (OffLineActivityResult.TotalResultInfo) result.getCompareData();
        dto.setSourceTotal(resultInfo.getSourceTotal());
        dto.setTargetTotal(resultInfo.getTargetTotal());
        dto.setVerifyResult(OffLineActivityVerifyResult.of(result.getVerifyResult().getCode()));
        dto.setSourceCompareTotalKeys(result.getCompareKeys().getSourceCompareKeys());
        dto.setTargetCompareTotalKeys(result.getCompareKeys().getTargetCompareKeys());
        dto.setResultType(OffLineActivityResultType.TOTAL);
        return dto;
    }

    public static OffLineTaskDetailResultDTO adapt2OffLineTaskDetailResultDTO(OffLineActivityResult result) {
        OffLineTaskDetailResultDTO dto = new OffLineTaskDetailResultDTO();
        OffLineActivityResult.DetailResultInfo resultInfo = (OffLineActivityResult.DetailResultInfo) result.getCompareData();
        dto.setSourceUnique(resultInfo.getSourceUnique());
        dto.setTargetUnique(resultInfo.getTargetUnique());
        dto.setInConsistency(resultInfo.getInConsistency());
        dto.setVerifyResult(OffLineActivityVerifyResult.of(result.getVerifyResult().getCode()));
        dto.setSourceCompareDetailKeys(result.getCompareKeys().getSourceCompareKeys());
        dto.setTargetCompareDetailKeys(result.getCompareKeys().getTargetCompareKeys());
        dto.setResultType(OffLineActivityResultType.DETAIL);
        return dto;
    }

}
