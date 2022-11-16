package com.source.rworkflow.workflow.domain.approval;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.type.ApprovalStatusType;
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

        trigger.createAssignees(assignees, requestId, created.getId());

        return created;
    }

    public WorkflowRequestApproval createUrgent(final Long requestId, final WorkflowRequestApproval approval) {
        final var created = service.createUrgent(approval);

        trigger.afterUrgentCreate(requestId, created.getId());

        return created;
    }

    public WorkflowRequestApproval approve(final WorkflowRequestApproval approval, final SessionUserId sessionUserId) {
        final var approvalFinish = trigger.assigneeApprove(approval, sessionUserId);

        if (approvalFinish) {
            approval.setStatus(ApprovalStatusType.APPROVED);

            trigger.setExecutionPending(approval.getRequestId(), sessionUserId);
        } else {
            approval.setStatus(ApprovalStatusType.IN_PROGRESS);
        }

        return service.approve(approval);
    }

    public WorkflowRequestApproval disApprove(final WorkflowRequestApproval approval, final SessionUserId sessionUserId) {
        trigger.assigneeDisApprove(approval, sessionUserId);

        approval.setStatus(ApprovalStatusType.REJECTED);

        return service.approve(approval);
    }
}
