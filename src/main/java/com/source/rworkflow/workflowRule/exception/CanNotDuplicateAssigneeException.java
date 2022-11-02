package com.source.rworkflow.workflowRule.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class CanNotDuplicateAssigneeException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRule.CANNOT_DUPLICATE_ASSIGNEE;

    public CanNotDuplicateAssigneeException() {
        super(exceptionCode);
    }
}
