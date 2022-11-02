package com.source.rworkflow.workflowRule.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class AssigneeCanNotBeEmpty extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRule.ASSIGNEE_CAN_NOT_BE_EMPTY;

    public AssigneeCanNotBeEmpty() {
        super(exceptionCode);
    }
}
