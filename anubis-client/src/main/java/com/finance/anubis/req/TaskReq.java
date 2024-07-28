package com.finance.anubis.req;

import com.guming.api.pojo.page.PageRequest;
import com.finance.anubis.field.CommonTaskConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 14:15
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskReq extends PageRequest implements Serializable {

    private Long id;

    /**
     * 实时对账配置
     */
    private CommonTaskConfig config;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 任务类型
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
