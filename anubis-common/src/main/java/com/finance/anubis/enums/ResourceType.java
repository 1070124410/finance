package com.finance.anubis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 对比事件数据源类型
 *
 * @author longchuan
 * @date 2022年12月28日17:56:43
 */
@AllArgsConstructor
public enum ResourceType {

    URLResourceConfig("URL_RESOURCE_CONFIG", "url类型资源"),
    MessageResourceConfig("MESSAGE_RESOURCE_CONFIG", "消息类型资源"),
    EventResourceConfig("EVENT_RESOURCE_CONFIG", "事件类型资源"),
    ;


    @Getter
    private final String code;
    @Getter
    private final String name;

    public static ResourceType of(String code) {
        return Arrays.stream(ResourceType.values()).filter(resourceType -> resourceType.code.equals(code)).findFirst().orElse(null);
    }
}
