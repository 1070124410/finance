package com.finance.anubis.repository.entity;

import com.finance.anubis.core.constants.enums.Action;
import com.finance.anubis.core.constants.enums.OffLineAction;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TaskActivityEntity implements Serializable {

    /**
     * 主键
     */
    private Long id;
    /**
     * 业务主键
     */
    private String bizKey;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 比对任务上下文
     */
    private String context;
    /**
     * 下一活动行为
     */
    private Action action;

    /**
     * 离线对账下一活动行为
     */
    private OffLineAction offLineAction;
    /**
     * 任务执行id
     */
    private Long taskConfigId;

    /**
     * 当前执行次数
     */
    private Integer times;
}
