package com.finance.anubis.req;

import com.guming.api.pojo.page.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author yezhaoyang
 * @Date 2023/03/23 15:20
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OffLineActivityResultReq extends PageRequest implements Serializable {

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
    private String resultType;

    /**
     * 对账结果
     */
    private String verifyResult;

    /**
     * 业务主键
     */
    private String bizKey;

}
