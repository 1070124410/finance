package com.finance.anubis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author yezhaoyang
 * @Date 2023/03/09 11:37
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OffLineTaskActivityResultDTO implements Serializable {

    /**
     * 总账结果数据
     */
    private OffLineTaskTotalResultDTO totalResult;

    /**
     * 细账结果数据
     */
    private OffLineTaskDetailResultDTO detailResult;

    /**
     * 上游自定义参数,对账完成mq返回
     */
    private String sourceCustom;

    /**
     * 上游自定义参数,对账完成mq返回
     */
    private String targetCustom;
}

