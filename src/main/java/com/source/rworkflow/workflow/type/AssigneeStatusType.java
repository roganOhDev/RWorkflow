package com.source.rworkflow.workflow.type;

public enum AssigneeStatusType {
    None(false, true, true),
    PENDING(true, true, true),
    APPROVED(true, true, true),
    REJECTED(true, true, true),
    IN_PROGRESS(false, true, true),
    SUCCEEDED(false, true, false),
    FAILED(false, true, false),
    AUTO_CONFIRMED(true, true, false),
    EXPIRED(true, true, false);

    boolean approval;
    boolean execution;
    boolean review;

    AssigneeStatusType(boolean approval, boolean execution, boolean review) {
    }

    public boolean canUsedForApprovalAssignee(AssigneeStatusType type) {
        return type.approval;
    }

    public boolean canUsedForExecutionAssignee(AssigneeStatusType type) {
        return type.execution;
    }

    public boolean canUsedForReviewAssignee(AssigneeStatusType type) {
        return type.review;
    }
}
