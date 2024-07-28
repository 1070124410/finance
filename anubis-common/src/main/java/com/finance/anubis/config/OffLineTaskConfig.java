package com.finance.anubis.config;

import lombok.Data;

/**
 * @Author yezhaoyang
 * @Date 2023/03/01 10:41
 * @Description
 **/
@Data
public class OffLineTaskConfig extends TaskConfig {

    private OffLineResourceConfig sourceConfig;

    private OffLineResourceConfig targetConfig;

    /**
     * 离线对账中对细账允许的错误阈值
     */
    private Integer errorThreshold;

    /**
     * 总账通过后是否对细账
     */
    private Boolean detailSwitch;


    /**
     * 错误重试次数
     */
    private Integer retryTime;


}
