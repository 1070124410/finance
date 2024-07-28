package com.finance.anubis.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum FileType {
    TXT("TXT"),
    CSV("CSV"),
    ;


    @Getter
    private final String code;



}
