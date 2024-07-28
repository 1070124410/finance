package com.finance.anubis.repository.entity;

import com.finance.anubis.core.constants.enums.ActionResult;
import com.finance.anubis.core.constants.enums.OffLineActivityResultType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OffLineTaskActivityResultEntity implements Serializable {

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
     * 用于比较的数据
     */
    private String compareData;

    /**
     * 比较keys
     */
    private String compareKeys;

    /**
     * 对账结果类型
     */
    private OffLineActivityResultType resultType;

    /**
     * 对账结果
     */
    private ActionResult verifyResult;

    /**
     * 业务主键
     */
    private String bizKey;

    /**
     * 当前执行次数
     */
    private Integer times;
}
