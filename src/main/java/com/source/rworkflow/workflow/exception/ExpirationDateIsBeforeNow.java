package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;
import com.source.rworkflow.workflow.type.WorkflowRequestType;

public class ExpirationDateIsBeforeNow extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.EXPIRATION_DATE_IS_BEFORE_NOW;

    public ExpirationDateIsBeforeNow(final String dateName) {
        super(exceptionCode + " : " + dateName);
    }
}
