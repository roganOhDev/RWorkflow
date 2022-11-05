package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class ApprovalNotFoundByOrderException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.APPROVAL_NOT_FOUND_BY_ORDER_EXCEPTION;

    public ApprovalNotFoundByOrderException(final Long requestId, final Long order) {
        super(exceptionCode + " requestId : " + requestId + " order : " + order);
    }
}
