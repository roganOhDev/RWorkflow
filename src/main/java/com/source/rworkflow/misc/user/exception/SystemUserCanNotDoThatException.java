package com.source.rworkflow.misc.user.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class SystemUserCanNotDoThatException extends RException {
    private final static String exceptionCode = ExceptionCode.User.SYSTEM_USER_CAN_NOT_DO_THAT;

    public SystemUserCanNotDoThatException() {
        super(exceptionCode);
    }
}
