package com.finance.anubis.core.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum TaskStatus implements EnumBase {
    STOP("STOP", "停用"), START("START", "启用"),
    ;
    private final String code;
    private final String message;

    public static TaskStatus of(String code) {
        return Arrays.stream(TaskStatus.values()).filter(resourceType -> resourceType.code.equals(code)).findFirst().orElse(null);
    }
}
