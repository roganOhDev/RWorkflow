package com.source.rworkflow.workflowRule.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class CanNotDuplicateOrderException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRule.CANNOT_DUPLICATE_ORDER;

    public CanNotDuplicateOrderException() {
        super(exceptionCode);
    }
}
