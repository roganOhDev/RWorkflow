package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class CanNotActionException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.CAN_NOT_ACTION_EXCEPTION;

    public CanNotActionException(final String status) {
        super(exceptionCode + status);
    }
}
