package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class ApprovalAssigneeCanNotBeCreatedWhenUrgentException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRule.APPROVAL_ASSIGNEE_CAN_NOT_BE_CREATED_WHEN_URGENT;

    public ApprovalAssigneeCanNotBeCreatedWhenUrgentException() {
        super(exceptionCode);
    }
}
