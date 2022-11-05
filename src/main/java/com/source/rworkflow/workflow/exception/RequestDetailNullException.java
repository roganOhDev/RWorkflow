package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;
import com.source.rworkflow.workflow.type.WorkflowRequestType;

public class RequestDetailNullException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.REQUEST_DETAIL_NULL_EXCEPTION;

    public RequestDetailNullException(final WorkflowRequestType type) {
        super(exceptionCode + "  requestType: " + type.name());
    }
}
