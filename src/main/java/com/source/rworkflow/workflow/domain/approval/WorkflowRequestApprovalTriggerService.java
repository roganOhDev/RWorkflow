package com.source.rworkflow.workflow.domain.approval;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestApprovalTriggerService {
    private final WorkflowRequestApprovalTrigger trigger;
    private final WorkflowRequestApprovalService service;

    public WorkflowRequestApproval create(final List<Long> assignees, final Long requestId, final WorkflowRequestApproval approval) {
        trigger.beforeCreate(assignees, requestId, approval.getId());

        return service.create(approval);
    }
}
