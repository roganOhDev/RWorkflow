package com.source.rworkflow.workflowRule.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class CanNotDeleteWorkflowRuleException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRule.CANNOT_DELETE;

    public CanNotDeleteWorkflowRuleException(final String message) {
        super(exceptionCode + "  reason : " + message);
    }
}
