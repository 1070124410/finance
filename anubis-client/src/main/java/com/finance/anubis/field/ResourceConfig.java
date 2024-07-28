package com.finance.anubis.field;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author yezhaoyang
 * @Date 2023/01/16 14:37
 * @Description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "resourceType", visible =
        true)
@JsonSubTypes({@JsonSubTypes.Type(value = MessageResourceConfig.class, name = "MessageResourceConfig"),
        @JsonSubTypes.Type(value = EventResourceConfig.class, name = "EventResourceConfig"),
        @JsonSubTypes.Type(value = URLResourceConfig.class, name = "URLResourceConfig")})
public class ResourceConfig {
    private String key;
    /***
     * 配置类型
     */
    private String resourceType;

    private Map<String, String> dataMapping;
}
