package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;
import com.source.rworkflow.workflow.type.AssigneeStatusType;

public class AssigneeCanNotAction extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.ASSIGNEE_CAN_NOT_ACTION;

    public AssigneeCanNotAction(final String action, final AssigneeStatusType type) {
        super(exceptionCode+"  action : " + action + ", status : " +type.name());
    }
}
