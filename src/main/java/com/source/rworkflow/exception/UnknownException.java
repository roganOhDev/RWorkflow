package com.source.rworkflow.exception;

public class UnknownException extends RException {
    private final static String exceptionCode = ExceptionCode.Api.UNKNOWN_EXCEPTION;

    UnknownException() {
        super(exceptionCode);
    }

    UnknownException(String message) {
        super(exceptionCode + "  , message");
    }
}
