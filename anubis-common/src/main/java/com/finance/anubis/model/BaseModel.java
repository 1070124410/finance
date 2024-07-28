package com.finance.anubis.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseModel implements Serializable {
    protected Long id;

    /**
     * 创建时间
     */
    protected LocalDateTime createTime;
    /**
     * 更新时间
     */
    protected LocalDateTime updateTime;
}
