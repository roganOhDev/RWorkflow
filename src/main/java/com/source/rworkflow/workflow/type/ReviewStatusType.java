package com.source.rworkflow.workflow.type;

import lombok.Getter;

public enum ReviewStatusType {
    NONE(false),

    PENDING(true),
    IN_PROGRESS(true),

    CONFIRMED(false);

    @Getter
    private boolean proceeding;

    ReviewStatusType(boolean proceeding) {
    }
}
