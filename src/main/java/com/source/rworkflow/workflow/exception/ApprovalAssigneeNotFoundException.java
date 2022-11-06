package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class ApprovalAssigneeNotFoundException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.CAN_NOT_FOUND_APPROVAL_ASSIGNEE;

    public ApprovalAssigneeNotFoundException(Long requestId, Long approvalId, Long order, Long userId) {
        super(exceptionCode + "  requestId : " + requestId + ", approvalId : " + approvalId + ", order : " + order + ", userId" + userId);
    }
}
