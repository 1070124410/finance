package com.finance.anubis.res;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 14:44
 * @Description
 **/
@Data
public class TaskRes implements Serializable {

    private Long id;

    /**
     * 对账任务配置
     */
    private String config;

    /**
     * 任务状态:START,STOP
     */
    private String status;

    /**
     * 任务类型:ONLINE,OFFLINE
     */
    private String type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
