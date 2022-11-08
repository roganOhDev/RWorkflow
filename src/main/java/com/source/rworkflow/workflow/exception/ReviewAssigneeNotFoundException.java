package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class ReviewAssigneeNotFoundException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.REVIEW_ASSIGNEE_NOT_FOUND_EXCEPTION;

    public ReviewAssigneeNotFoundException(final Long assigneeId) {
        super(exceptionCode+"  requestAssigneeId : " + assigneeId);
    }
}
