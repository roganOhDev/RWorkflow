package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class OrderMustMatchWithOrderOfRuleException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.WORKFLOW_REQUEST_ORDER_MUST_MATCH_WITH_ORDER_OF_RULE;

    public OrderMustMatchWithOrderOfRuleException() {
        super(exceptionCode);
    }
}
