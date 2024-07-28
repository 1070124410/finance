package com.finance.anubis.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/03/29 10:09
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible =
        true)
@JsonSubTypes({@JsonSubTypes.Type(value = OnLineTaskConfig.class, name = "ONLINE"),
        @JsonSubTypes.Type(value = OffLineTaskConfig.class, name = "OFFLINE")})
public class TaskConfig {

    private String type;

    /**
     * 构成业务唯一所有的key列表，该列表应该完全来自于消息或者实践
     */
    private List<String> uniqueKeys;

    /**
     * 任务名称
     */
    private String name;

    private Long id;

    /**
     * 配置版本，每次更新后+1
     */
    private Integer version;


}
