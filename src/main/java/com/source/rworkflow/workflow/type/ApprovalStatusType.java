package com.source.rworkflow.workflow.type;

import lombok.Getter;

public enum ApprovalStatusType {
    PENDING(false),

    IN_PROGRESS(true),

    APPROVED(false),
    REJECTED(false),
    EXPIRED(false);

    @Getter
    private boolean proceeding;

    ApprovalStatusType(boolean proceeding) {
    }
}
