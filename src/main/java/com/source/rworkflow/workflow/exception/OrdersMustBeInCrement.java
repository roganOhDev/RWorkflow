package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class OrdersMustBeInCrement extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.ORDERS_MUST_BE_INCREMENT;

    public OrdersMustBeInCrement() {
        super(exceptionCode);
    }
}
