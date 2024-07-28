package com.finance.anubis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @Author yezhaoyang
 * @Date 2023/03/03 10:43
 * @Description 对账任务类型
 **/
@AllArgsConstructor
@Getter
public enum TaskType implements EnumBase {
    ONLINE("ONLINE", "实时任务"), OFFLINE("OFFLINE", "离线任务"),
    ;

    private final String code;
    private final String message;

    public static TaskType of(String code) {
        return Arrays.stream(TaskType.values()).filter(a -> a.code.equals(code)).findFirst().orElse(null);
    }
}
