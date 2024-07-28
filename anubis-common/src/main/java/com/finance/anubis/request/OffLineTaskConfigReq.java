package com.finance.anubis.request;

import com.finance.anubis.config.CommonTaskConfig;
import com.finance.anubis.config.OffLineResourceConfig;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author yezhaoyang
 * @Date 2023/03/22 14:26
 * @Description
 **/
@Data
public class OffLineTaskConfigReq extends CommonTaskConfig implements Serializable {

    /**
     * 数据配置
     */
    private OffLineResourceConfig sourceConfig;

    /**
     * 数据配置
     */
    private OffLineResourceConfig targetConfig;


    /**
     * 对账错误阈值
     */
    private int errorThreshold;

    /**
     * 细账开关
     */
    private Boolean detailSwitch;

    /**
     * 错误重试次数
     */
    private int retryTime;

}
