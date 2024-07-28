package com.finance.anubis.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnLineTaskConfig extends TaskConfig {

    /**
     * 源数据监听配置
     */
    private MessageResourceConfig sourceConfig;

    /**
     * 目标数据查询配置、目前只支持查询
     */
    private List<URLResourceConfig> targetConfigs;

    /**
     * 数据比较的key列表
     */
    private List<String> compareKeys;

    /**
     * 目标数据获取完成后延迟的时间，单位秒
     */
    private int delay;

    //重试次数
//    private int 重试time;

}
