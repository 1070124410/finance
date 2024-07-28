package com.finance.anubis.exception;

public class StatusCodeException extends RuntimeException {
    private final Status status;

    public StatusCodeException(Status status) {
        super(status.getMessage());
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }


    public Status getStatusCode() {
        return status;
    }
}