package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class WorkflowIsCanceledException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.WORKFLOW_IS_CANCELED;

    public WorkflowIsCanceledException(final Long requestId) {
        super(exceptionCode + " workflowRequestId : " + requestId);
    }
}
