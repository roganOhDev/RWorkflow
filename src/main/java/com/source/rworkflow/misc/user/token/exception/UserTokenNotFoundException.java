package com.source.rworkflow.misc.user.token.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class UserTokenNotFoundException extends RException {
    private final static String exceptionCode = ExceptionCode.UserToken.USER_TOKEN_NOT_FOUND_EXCEPTION;

    public UserTokenNotFoundException(String token) {
        super(exceptionCode + "  : " + token);
    }
}
