package com.finance.anubis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum ActionResult implements EnumBase {
    UNREADY("UNREADY", "未就绪"), ACCORD("ACCORD", "一致"), VARIANCE("VARIANCE", "不一致"), FAIL("FAIL", "失败");
    @Getter
    private final String code;
    @Getter
    private final String message;

    public static ActionResult of(String code) {
        return Arrays.stream(ActionResult.values()).filter(actionResult -> actionResult.code.equals(code)).findFirst().orElse(null);
    }
}
