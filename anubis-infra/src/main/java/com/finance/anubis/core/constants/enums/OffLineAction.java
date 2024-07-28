package com.finance.anubis.core.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/03/03 10:12
 * @Description 离线对账状态枚举
 **/
@AllArgsConstructor
public enum OffLineAction implements EnumBase {

    DATA_INIT("DATA_INIT", "初始化") {
        @Override
        public List<OffLineAction> originAction() {
            return Arrays.asList(DATA_DONE, DATA_FAIL);
        }
    },

    DATA_FETCH("DATA_FETCH", "数据获取") {
        @Override
        public List<OffLineAction> originAction() {
            return Arrays.asList(DATA_DONE, DATA_FAIL, DATA_INIT, DATA_FETCH);
        }
    },
    DATA_COMPARE_TOTAL("DATA_COMPARE_TOTAL", "总账数据比较") {
        @Override
        public List<OffLineAction> originAction() {
            return Arrays.asList(DATA_FETCH);
        }
    },
    DATA_COMPARE_DETAIL("DATA_COMPARE_DETAIL", "细账数据比较") {
        @Override
        public List<OffLineAction> originAction() {
            return Arrays.asList(DATA_COMPARE_TOTAL);
        }
    },
    DATA_DONE("DATA_DONE", "数据整理") {
        @Override
        public List<OffLineAction> originAction() {
            return Arrays.asList(DATA_COMPARE_DETAIL);
        }
    },
    DATA_FAIL("DATA_FAIL", "失败") {
        @Override
        public List<OffLineAction> originAction() {
            return Arrays.asList(DATA_FETCH, DATA_COMPARE_TOTAL, DATA_COMPARE_DETAIL);
        }
    };

    @Getter
    private final String code;
    @Getter
    private final String message;

    public abstract List<OffLineAction> originAction();

    public static OffLineAction of(String code) {
        return Arrays.stream(OffLineAction.values()).filter(a -> a.code.equals(code)).findFirst().orElse(null);
    }

}
