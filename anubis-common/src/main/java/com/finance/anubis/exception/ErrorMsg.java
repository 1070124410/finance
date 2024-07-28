package com.finance.anubis.exception;

import com.finance.anubis.enums.OffLineAction;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yezhaoyang
 * @Date 2023/03/08 11:56
 * @Description
 **/
@Data
@NoArgsConstructor
public class ErrorMsg {

    private String bizKey;

    private String configKey;

    private OffLineAction action;

    private String errorMessage;

    private Throwable exception;

    public ErrorMsg(String bizKey, String configKey, OffLineAction action, String errorMessage) {
        this.bizKey = bizKey;
        this.configKey = configKey;
        this.action = action;
        this.errorMessage = errorMessage;
    }

    public ErrorMsg(String bizKey, String configKey, OffLineAction action, String errorMessage, Throwable exception) {
        this.bizKey = bizKey;
        this.configKey = configKey;
        this.action = action;
        this.errorMessage = errorMessage;
        this.exception = exception;
    }
}
