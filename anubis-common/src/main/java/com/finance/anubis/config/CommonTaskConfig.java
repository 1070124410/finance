package com.finance.anubis.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.finance.anubis.request.OffLineTaskConfigReq;
import com.finance.anubis.request.TaskConfigReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/03/29 10:01
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible =
        true)
@JsonSubTypes({@JsonSubTypes.Type(value = TaskConfigReq.class, name = "ONLINE"),
        @JsonSubTypes.Type(value = OffLineTaskConfigReq.class, name = "OFFLINE")})
public class CommonTaskConfig implements Serializable {
    /**
     * 任务类型
     */
    private String type;

    private Long id;

    /**
     * 唯一键
     */
    private List<String> uniqueKeys;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 配置版本
     */
    private Integer version;
}
