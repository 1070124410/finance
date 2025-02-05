package com.finance.anubis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum Cmd implements EnumBase {
    STOP("STOP", "停用"), START("START", "启用"),
    ;
    private final String code;
    private final String message;

    public static Cmd of(String code) {
        return Arrays.stream(Cmd.values()).filter(resourceType -> resourceType.code.equals(code)).findFirst().orElse(null);
    }
}
