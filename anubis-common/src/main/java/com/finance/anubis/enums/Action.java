package com.finance.anubis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum Action implements EnumBase {
    INIT("INIT", "初始化") {
        @Override
        public Action nextAction() {
            return SOURCE_FETCH;
        }
    },
    REMARK("REMARK", "重新执行") {
        @Override
        public Action nextAction() {
            return SOURCE_FETCH;
        }
    },
    SOURCE_FETCH("SOURCE_FETCH", "获取源URL响应") {
        @Override
        public Action nextAction() {
            return TARGET_FETCH;
        }
    },
    TARGET_FETCH("TARGET_FETCH", "获取目标URL响应") {
        @Override
        public Action nextAction() {
            return COMPARE;
        }
    },

    COMPARE("COMPARE", "比较") {
        @Override
        public Action nextAction() {
            return DONE;
        }
    },
    DONE("DONE", "完成") {
        @Override
        public Action nextAction() {
            return null;
        }
    },
    ;

    public abstract Action nextAction();

    @Getter
    private final String code;
    @Getter
    private final String message;

    public static Action of(String code) {
        return Arrays.stream(Action.values()).filter(actionResult -> actionResult.code.equals(code)).findFirst().orElse(null);
    }

}
