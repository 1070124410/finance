package com.finance.anubis.core.constants.enums;

import java.util.Arrays;

/**
 * @Author yezhaoyang
 * @Date 2023/03/09 09:29
 * @Description
 **/
public enum OffLineActivityResultType {
    /**
     * 总账结果
     */
    TOTAL,
    /**
     * 细账结果
     */
    DETAIL;

    public static OffLineActivityResultType of(String code) {
        return Arrays.stream(OffLineActivityResultType.values()).filter(type -> type.name().equals(code)).findFirst().orElse(null);
    }

}
