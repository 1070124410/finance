package com.finance.anubis.request;

import com.finance.anubis.config.CommonTaskConfig;
import com.finance.anubis.config.MessageResourceConfig;
import com.finance.anubis.config.URLResourceConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 14:36
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskConfigReq extends CommonTaskConfig implements Serializable {

    /**
     * 源数据配置
     */
    private MessageResourceConfig sourceConfig;

    /**
     * 目标数据配置
     */
    private List<URLResourceConfig> targetConfigs;

    /**
     * 对账字段
     */
    private List<String> compareKeys;

    /**
     * 拉取target数据延迟时间
     */
    private Integer delay;

}
