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
public class OffLineActivityResultRes implements Serializable {

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
     * 总账结果数据
     */
    private String totalResult;

    /**
     * 细账结果数据
     */
    private String detailResult;

    /**
     * 业务主键
     */
    private String bizKey;

    /**
     * 当前执行次数
     */
    private Integer times;
}
