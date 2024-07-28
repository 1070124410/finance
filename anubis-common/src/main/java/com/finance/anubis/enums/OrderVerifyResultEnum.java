package com.finance.anubis.enums;

/**
 * @Classname OrderVerifyResultEnum
 * @Date 2022/6/17 16:47
 * @author tianxu
 */
public enum OrderVerifyResultEnum {
    // 无差异
    COINCIDENT,
    // source独占
    SOURCE_UNIQUE,
    // target独占
    TARGET_UNIQUE,
    // 两者都有,字段对不上
    DIFFERENT;

}
