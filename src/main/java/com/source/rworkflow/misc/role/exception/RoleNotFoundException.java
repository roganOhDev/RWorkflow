package com.source.rworkflow.misc.role.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class RoleNotFoundException extends RException {
    private final static String exceptionCode = ExceptionCode.Role.ROLE_NOT_FOUND_EXCEPTION;

    public RoleNotFoundException() {
        super(exceptionCode);
    }

    public RoleNotFoundException(Long id) {
        super(exceptionCode + " : " + id);
    }
}
