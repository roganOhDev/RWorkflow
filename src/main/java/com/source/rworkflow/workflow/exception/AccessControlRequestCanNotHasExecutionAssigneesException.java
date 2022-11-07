package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class AccessControlRequestCanNotHasExecutionAssigneesException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.ACCESS_CONTROL_REQUEST_CAN_NOT_HAS_EXECUTION_ASSIGNEES;

    public AccessControlRequestCanNotHasExecutionAssigneesException() {
        super(exceptionCode);
    }
}
