package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class AccessControlRequestCanNotBeUrgent extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.ACCESS_CONTROL_CAN_NOT_BE_URGENT;

    public AccessControlRequestCanNotBeUrgent() {
        super(exceptionCode);
    }
}
