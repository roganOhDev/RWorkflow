package com.source.rworkflow.workflowRule.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class OrderValueException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRule.ORDER_VALUE_EXCEPTION;

    public OrderValueException() {
        super(exceptionCode);
    }
}
