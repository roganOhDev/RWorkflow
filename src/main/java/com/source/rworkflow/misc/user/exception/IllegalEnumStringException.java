package com.source.rworkflow.misc.user.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class IllegalEnumStringException extends RException {
    private final static String exceptionCode = ExceptionCode.Api.ILLEGAL_ENUM_STRING_EXCEPTION;

    public IllegalEnumStringException() {
        super(exceptionCode);
    }

    public IllegalEnumStringException(String message) {
        super(exceptionCode + "  : " + message);
    }
}
