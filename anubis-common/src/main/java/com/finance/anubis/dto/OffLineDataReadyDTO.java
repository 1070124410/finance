package com.finance.anubis.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class OffLineDataReadyDTO implements Serializable {

    /**
     * 任务名
     */
    private String taskName;

    /**
     * 对账配置key
     */
    private String configKey;

    /**
     * 对账数据请求参数
     */
    private String requestParam;

    /**
     * 上游自定义参数,对账完成mq返回
     */
    private String custom;

    /**
     * 对账日期(为空则默认当天,使用LocalDateTime类型,格式如"2023-03-18T00:00:00" 必传字段)
     */
    private String verifyDate;

}
