package com.finance.anubis.res;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 14:15
 * @Description
 **/
@Data
public class ActivityResultRes implements Serializable {

    /**
     * 主键
     */
    private Long id;
    /**
     * 任务执行记录id
     */
    private Long taskActivityId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 用于比较的源数据
     */
    private String compareSourceData;
    /**
     * 用于比较的目标数据
     */
    private String compareTargetData;
    /**
     * 比较keys
     */
    private String compareKeys;

    /**
     * 不一致的列
     */
    private String varianceKeys;

    /**
     * 结果类型
     */
    private String actionResult;

    /**
     * 业务主键
     */
    private String bizKey;

    /**
     * 当前执行次数
     */
    private Integer times;
}
