package com.finance.anubis.core.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 离线对账对比事件数据源类型
 *
 */
@AllArgsConstructor
public enum OffLineResourceType implements EnumBase {

    OffLineFileResource("OffLineFileResource", "文件类型资源"),
    OffLineHttpResource("OffLineHttpResource", "接口类型资源"),
    ;


    @Getter
    private final String code;
    @Getter
    private final String message;

    public static OffLineResourceType of(String code) {
        return Arrays.stream(OffLineResourceType.values()).filter(resourceType -> resourceType.code.equals(code)).findFirst().orElse(null);
    }


}
