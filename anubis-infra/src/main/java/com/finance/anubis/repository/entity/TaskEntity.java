package com.finance.anubis.repository.entity;

import com.finance.anubis.enums.TaskStatus;
import com.finance.anubis.enums.TaskType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class TaskEntity implements Serializable {

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
     * 配置id
     */
    private Long taskConfigId;
    /**
     * 任务状态
     */
    private TaskStatus status;

    /**
     * 任务类型
     */
    private TaskType type;


}
