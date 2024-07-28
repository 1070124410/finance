package com.finance.anubis.core.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.finance.anubis.core.constants.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 任务资源配置抽象类
 *
 * @author longchuan
 * @data 2022年12月28日17:50:52
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "resourceType", visible =
        true)
@JsonSubTypes({@JsonSubTypes.Type(value = MessageResourceConfig.class, name = "MessageResourceConfig"),
        @JsonSubTypes.Type(value = EventResourceConfig.class, name = "EventResourceConfig"),
        @JsonSubTypes.Type(value = URLResourceConfig.class, name = "URLResourceConfig")})
public abstract class ResourceConfig {
    private String key;
    /***
     * 配置类型
     */
    private ResourceType resourceType;

    private Map<String, String> dataMapping;

    public ResourceConfig(ResourceType resourceType, Map<String, String> dataMapping,
                          String key) {
        this.resourceType = resourceType;
        this.dataMapping = dataMapping;
        this.key = key;

    }
}

