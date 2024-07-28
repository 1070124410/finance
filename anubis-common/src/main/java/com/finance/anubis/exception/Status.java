package com.finance.anubis.exception;

public class Status {
    private final int code;
    private final String message;

    private Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Status create(int code, String message) {
        return new Status(code, message);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static Status error(String msg){
        return Status.create(StatusCodeEnum.FAIL.getCode(), msg);
    }

    public String getReason() {
        return message;
    }
}
