package com.source.rworkflow.common.exception;

public class IllegalEnumStringException extends RException {
    private final static String exceptionCode = ExceptionCode.Api.ILLEGAL_ENUM_STRING_EXCEPTION;

    public IllegalEnumStringException() {
        super(exceptionCode);
    }

    public IllegalEnumStringException(String message) {
        super(exceptionCode + "  : " + message);
    }
}
