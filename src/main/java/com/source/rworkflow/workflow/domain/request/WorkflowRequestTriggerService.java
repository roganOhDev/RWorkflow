package com.source.rworkflow.workflow.domain.request;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import com.source.rworkflow.workflow.type.ApprovalStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestTriggerService {
    private final WorkflowRequestTrigger trigger;
    private final WorkflowRequestService service;

    public WorkflowRequest create(final WorkflowRequest workflowRequest, final WorkflowRequestDto.Create.Request request, final SessionUserId sessionUserId) {
        final var created = service.create(workflowRequest, sessionUserId);

        trigger.afterCreate(created.getId(), request);

        return created;
    }

    public WorkflowRequest approveOk(final WorkflowRequest workflowRequest, final Long order, final SessionUserId sessionUserId) {
        final var approvalCount = trigger.beforeApprove(workflowRequest.getId(), order, sessionUserId, true);

        if (order == approvalCount) {
            workflowRequest.setApprovalStatus(ApprovalStatusType.APPROVED);
        } else {
            workflowRequest.setApprovalStatus(ApprovalStatusType.IN_PROGRESS);
        }

        return service.approve(workflowRequest, sessionUserId);
    }

    public WorkflowRequest approveReject(final WorkflowRequest workflowRequest, final Long order, final SessionUserId sessionUserId) {
        trigger.beforeApprove(workflowRequest.getId(), order, sessionUserId, false);

        workflowRequest.setApprovalStatus(ApprovalStatusType.REJECTED);

        return service.approve(workflowRequest, sessionUserId);
    }

}
