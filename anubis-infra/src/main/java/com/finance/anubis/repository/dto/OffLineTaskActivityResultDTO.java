package com.finance.anubis.repository.dto;

import com.finance.anubis.core.task.model.OffLineActivityResult;
import com.finance.anubis.repository.entity.OffLineTaskActivityResultEntity;
import com.guming.api.json.JsonUtil;

/**
 * @Author yezhaoyang
 * @Date 2023/01/13 10:02
 * @Description
 **/
public class OffLineTaskActivityResultDTO {

    public static OffLineTaskActivityResultEntity toEntity(OffLineActivityResult result) {
        OffLineTaskActivityResultEntity entity = new OffLineTaskActivityResultEntity();
        entity.setId(result.getId());
        entity.setTaskActivityId(result.getTaskActivityId());
        entity.setBizKey(result.getBizKey());
        entity.setTimes(result.getTimes());
        entity.setResultType(result.getResultType());
        entity.setVerifyResult(result.getVerifyResult());
        entity.setCompareKeys(JsonUtil.toJson(result.getCompareKeys()));
        entity.setCompareData(JsonUtil.toJson(result.getCompareData()));
        entity.setCreateTime(result.getCreateTime());
        entity.setUpdateTime(result.getUpdateTime());

        return entity;
    }


    public static OffLineActivityResult toModel(OffLineTaskActivityResultEntity entity) {
        OffLineActivityResult result=new OffLineActivityResult();
        result.setId(entity.getId());
        result.setCreateTime(entity.getCreateTime());
        result.setUpdateTime(entity.getUpdateTime());
        result.setBizKey(entity.getBizKey());
        result.setTimes(entity.getTimes());
        result.setVerifyResult(entity.getVerifyResult());
        result.setResultType(entity.getResultType());
        result.setTaskActivityId(entity.getTaskActivityId());
        result.setCompareData(JsonUtil.of(entity.getCompareData(), OffLineActivityResult.ResultInfo.class));
        result.setCompareKeys(JsonUtil.of(entity.getCompareKeys(), OffLineActivityResult.ResultKey.class));

        return result;
    }
}
