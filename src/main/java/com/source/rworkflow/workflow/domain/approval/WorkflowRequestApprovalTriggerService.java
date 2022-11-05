package com.source.rworkflow.workflow.domain.approval;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.type.ApprovalStatusType;
import com.source.rworkflow.workflowRule.type.ApproveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestApprovalTriggerService {
    private final WorkflowRequestApprovalTrigger trigger;
    private final WorkflowRequestApprovalService service;

    public WorkflowRequestApproval create(final List<Long> assignees, final Long requestId, final WorkflowRequestApproval approval) {

        final var created = service.create(approval);

        trigger.afterCreate(assignees, requestId, created.getId());

        return created;
    }

    public WorkflowRequestApproval approveOk(final WorkflowRequestApproval approval, final SessionUserId sessionUserId) {
        trigger.beforeApprove(approval, sessionUserId, true);

        if (approval.getApproveType().equals(ApproveType.ALL)) {
            approval.setStatus(ApprovalStatusType.IN_PROGRESS);
        } else {
            approval.setStatus(ApprovalStatusType.APPROVED);
        }

        return service.approve(approval);
    }

    public WorkflowRequestApproval approveReject(final WorkflowRequestApproval approval, final SessionUserId sessionUserId) {
        trigger.beforeApprove(approval, sessionUserId, false);

        approval.setStatus(ApprovalStatusType.REJECTED);

        return service.approve(approval);
    }
}
