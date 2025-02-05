package com.finance.anubis.adapter;

import com.finance.anubis.config.*;
import com.finance.anubis.request.OffLineTaskConfigReq;
import com.finance.anubis.request.TaskConfigReq;
import com.finance.anubis.utils.JsonUtil;


///**
// * @Author yezhaoyang
// * @Date 2023/03/22 14:36
// * @Description
// **/
public class TaskConfigAdapter {


    public static OnLineTaskConfig toOnLineConfig(TaskConfigReq req) {
        if (null == req) {
            return new OnLineTaskConfig();
        }
        OnLineTaskConfig config = new OnLineTaskConfig();
        config.setSourceConfig(JsonUtil.of(JsonUtil.toJson(req.getSourceConfig()), MessageResourceConfig.class));
        config.setTargetConfigs(JsonUtil.ofList(JsonUtil.toJson(req.getTargetConfigs()), URLResourceConfig.class));
        config.setId(req.getId());
        config.setName(req.getName());
        config.setDelay(req.getDelay());
        config.setVersion(req.getVersion());
        config.setCompareKeys(req.getCompareKeys());
        config.setUniqueKeys(req.getUniqueKeys());

        return config;
    }

    public static OffLineTaskConfig toOffLineConfig(OffLineTaskConfigReq req) {
        if (null == req) {
            return new OffLineTaskConfig();
        }
        OffLineTaskConfig config = new OffLineTaskConfig();
        config.setId(req.getId());
        config.setName(req.getName());
        config.setSourceConfig(JsonUtil.of(JsonUtil.toJson(req.getSourceConfig()), OffLineResourceConfig.class));
        config.setTargetConfig(JsonUtil.of(JsonUtil.toJson(req.getTargetConfig()), OffLineResourceConfig.class));
        config.setRetryTime(req.getRetryTime());
        config.setErrorThreshold(req.getErrorThreshold());
        config.setDetailSwitch(req.getDetailSwitch());
        config.setVersion(req.getVersion());
        config.setUniqueKeys(req.getUniqueKeys());

        return config;
    }

}
