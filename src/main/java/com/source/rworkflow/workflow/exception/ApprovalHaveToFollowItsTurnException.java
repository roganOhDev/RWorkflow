package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class ApprovalHaveToFollowItsTurnException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.APPROVAL_HAVE_TO_FOLLOW_ITS_TURN_EXCEPTION;

    public ApprovalHaveToFollowItsTurnException() {
        super(exceptionCode);
    }
}
