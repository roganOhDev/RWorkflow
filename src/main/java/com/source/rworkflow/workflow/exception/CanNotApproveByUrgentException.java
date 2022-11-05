package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class CanNotApproveByUrgentException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.CAN_NOT_APPROVE_BY_URGENT_EXCEPTION;

    public CanNotApproveByUrgentException() {
        super(exceptionCode);
    }
}
