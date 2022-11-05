package com.source.rworkflow.workflow.type;

import lombok.Getter;

public enum ExecutionStatusType {
    NONE (false),

    PENDING(true),
    IN_PROGRESS(true),

    SUCCEEDED(false),
    FAILED(false),
    EXPIRED(false);

    @Getter
    private boolean proceeding;

    ExecutionStatusType(boolean proceeding) {
    }
}
