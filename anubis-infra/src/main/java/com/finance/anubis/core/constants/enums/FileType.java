package com.finance.anubis.core.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author yezhaoyang
 * @Date 2023/03/07 19:13
 * @Description
 **/
@AllArgsConstructor
@Getter
public enum FileType {
    TXT("TXT"),
    CSV("CSV"),
    ;


    @Getter
    private final String code;



}
