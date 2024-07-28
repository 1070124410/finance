package com.finance.anubis.repository.entity;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskConfigEntity implements Serializable {

    /**
     * 主键
     */
    private Long id;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 任务比对源数据配置
     */
    private String sourceConfig;
    /**
     * 任务比对目标源配置
     */
    private String targetConfig;
    /**
     * 比对属性列表
     */

    private List<String> compareKeys;
    /**
     * 延迟时间（秒）
     */
    private Integer delay;
    /**
     * 数据源唯一属性，compareKeys 子集
     */
    private List<String> uniqueKeys;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 配置版本
     */
    private Integer version;

    /**
     * 细账开关
     */
    private Boolean detailSwitch;

    /**
     * 错误阈值
     */
    private Integer errorThreshold;

    /**
     * 错误重试次数
     */
    private Integer retryTime;

}
