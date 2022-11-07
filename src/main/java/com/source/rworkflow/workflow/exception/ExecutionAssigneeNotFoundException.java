package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;
import com.source.rworkflow.workflow.type.AssigneeStatusType;

public class ExecutionAssigneeNotFoundException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.EXECUTION_ASSIGNEE_NOT_FOUND_EXCEPTION;

    public ExecutionAssigneeNotFoundException(final Long assigneeId) {
        super(exceptionCode+"  requestAssigneeId : " + assigneeId);
    }
}
