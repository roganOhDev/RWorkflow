package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class UrgentValueNotMatchException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.URGENT_VALUE_NOT_MATCH_EXCEPTION;

    public UrgentValueNotMatchException() {
        super(exceptionCode);
    }
}
