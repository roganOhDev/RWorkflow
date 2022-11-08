package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class AccessControlRequestNotFoundException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.ACCESS_CONTROL_REQUEST_NOT_FOUND_EXCEPTION;

    public AccessControlRequestNotFoundException(final Long requestId) {
        super(exceptionCode + " workflowRequestId : " + requestId);
    }
}
