package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class ExecutionExpirationDateMustBeAfterRequestExpirationDateException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.EXECUTION_EXPIRATION_DATE_MUST_BE_AFTER_REQUEST_EXPIRATION_DATE;

    public ExecutionExpirationDateMustBeAfterRequestExpirationDateException() {
        super(exceptionCode);
    }
}
