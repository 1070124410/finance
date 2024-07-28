package com.finance.anubis.enums;

import java.util.Arrays;

/**
 * @Author yezhaoyang
 * @Date 2023/03/08 15:43
 * @Description
 **/
public enum OffLineActivityVerifyResult {

    /**
     * 对账异常
     */
    ERROR,
    /**
     * 对账通过
     */
    ACCORD,
    /**
     * 对账未通过
     */
    VARIANCE;

    public static OffLineActivityVerifyResult of(String code) {
        return Arrays.stream(OffLineActivityVerifyResult.values()).filter(type -> type.name().equals(code)).findFirst().orElse(null);
    }
}
