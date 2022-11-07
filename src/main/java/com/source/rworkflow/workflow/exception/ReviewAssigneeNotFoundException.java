package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class ReviewAssigneeNotFoundException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRule.APPROVAL_ASSIGNEE_CAN_NOT_BE_CREATED_WHEN_URGENT;

    public ReviewAssigneeNotFoundException(final Long assigneeId) {
        super(exceptionCode+"  requestAssigneeId : " + assigneeId);
    }
}
