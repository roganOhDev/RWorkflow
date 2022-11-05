package com.source.rworkflow.workflow.domain.approval;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.domain.approval.assignee.WorkflowRequestApprovalAssignee;
import com.source.rworkflow.workflow.domain.approval.assignee.WorkflowRequestApprovalAssigneeCompositeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestApprovalTrigger {
    private final WorkflowRequestApprovalAssigneeCompositeService workflowRequestApprovalAssigneeCompositeService;

    public List<WorkflowRequestApprovalAssignee> afterCreate(final List<Long> ids, final Long requestId, final Long approvalId) {
        return workflowRequestApprovalAssigneeCompositeService.createCollection(ids, requestId, approvalId);
    }

    public WorkflowRequestApprovalAssignee beforeApprove(final WorkflowRequestApproval approval, final SessionUserId sessionUserId, final boolean approve) {
        return workflowRequestApprovalAssigneeCompositeService.approve(approval, sessionUserId, approve);
    }
}
