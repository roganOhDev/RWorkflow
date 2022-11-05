package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class WorkflowRequestNotFoundException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.WORKFLOW_REQUEST_NOT_FOUND;

    public WorkflowRequestNotFoundException(final Long id) {
        super(exceptionCode + " : " + id);
    }
}
