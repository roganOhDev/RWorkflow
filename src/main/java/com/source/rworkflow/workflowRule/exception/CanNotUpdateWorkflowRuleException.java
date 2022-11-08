package com.source.rworkflow.workflowRule.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class CanNotUpdateWorkflowRuleException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRule.CANNOT_UPDATE;

    public CanNotUpdateWorkflowRuleException(final String message) {
        super(exceptionCode + "  reason : " + message);
    }
}
