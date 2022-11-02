package com.source.rworkflow.misc.user.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class UserNotFoundException extends RException {
    private final static String exceptionCode = ExceptionCode.User.USER_NOT_FOUND_EXCEPTION;

    public UserNotFoundException() {
        super(exceptionCode);
    }

    public UserNotFoundException(Long id) {
        super(exceptionCode + " : " + id);
    }
}
