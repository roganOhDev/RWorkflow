package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class CanNotDuplicateRequestAccessControlConnectionException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.CAN_NOT_DUPLICATE_REQUEST_ACCESS_CONTROL_CONNECTION_EXCEPTION;

    public CanNotDuplicateRequestAccessControlConnectionException() {
        super(exceptionCode);
    }
}
