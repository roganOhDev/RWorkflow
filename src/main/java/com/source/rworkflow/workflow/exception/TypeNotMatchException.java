package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class TypeNotMatchException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.TYPE_NOT_MATCH_EXCEPTION;

    public TypeNotMatchException() {
        super(exceptionCode);
    }
}
