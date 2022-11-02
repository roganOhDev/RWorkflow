package com.source.rworkflow.workflowRule.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class WorkflowRuleNotFoundException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRule.WORKFLOW_RULE_NOT_FOUND;

    public WorkflowRuleNotFoundException() {
        super(exceptionCode);
    }

    public WorkflowRuleNotFoundException(Long id) {
        super(exceptionCode + " : " + id);
    }
}
