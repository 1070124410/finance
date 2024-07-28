package com.finance.anubis.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 14:15
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResultReq implements Serializable {

    /**
     * 主键
     */
    private Long id;
    /**
     * 任务执行记录id
     */
    private Long taskActivityId;

    /**
     * 结果类型
     */
    private String actionResult;

    /**
     * 业务主键
     */
    private String bizKey;

}
