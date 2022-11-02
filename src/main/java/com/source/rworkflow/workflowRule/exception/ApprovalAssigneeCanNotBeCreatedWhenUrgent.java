package com.source.rworkflow.workflowRule.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class ApprovalAssigneeCanNotBeCreatedWhenUrgent extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRule.APPROVAL_ASSIGNEE_CAN_NOT_BE_CREATED_WHEN_URGENT;

    public ApprovalAssigneeCanNotBeCreatedWhenUrgent() {
        super(exceptionCode);
    }
}
