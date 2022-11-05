package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class AssigneeCanNotBeEmpty extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.ASSIGNEE_CAN_NOT_BE_EMPTY;

    public AssigneeCanNotBeEmpty(final String type) {
        super(exceptionCode + " : " + type);
    }
}
