package com.source.rworkflow.workflow.exception;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.common.exception.ExceptionCode;
import com.source.rworkflow.common.exception.RException;

public class SelfApproveException extends RException {
    private final static String exceptionCode = ExceptionCode.WorkflowRequest.SELF_APPROVE_EXCEPTION;

    public SelfApproveException(final SessionUserId sessionUserId) {
        super(exceptionCode + " : " + sessionUserId.getId());
    }
}
