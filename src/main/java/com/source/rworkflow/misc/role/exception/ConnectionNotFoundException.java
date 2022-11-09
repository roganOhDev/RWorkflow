package com.source.rworkflow.misc.role.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class ConnectionNotFoundException extends RException {
    private final static String exceptionCode = ExceptionCode.Role.ConectionNotFoundException;

    public ConnectionNotFoundException() {
        super(exceptionCode);
    }

    public ConnectionNotFoundException(Long id) {
        super(exceptionCode + " : " + id);
    }
}
