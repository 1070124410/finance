package com.finance.anubis.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 14:15
 * @Description
 **/
@Data
public class OffLineTaskActivityRes implements Serializable {

    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    /**
     * 对账数据上下文
     */
    private String context;

    /**
     * 离线任务配置id
     */
    private Long taskConfigId;

    /**
     * 对账阶段
     */
    private String action;

    /**
     * 重试次数
     */
    private Integer times;
}
