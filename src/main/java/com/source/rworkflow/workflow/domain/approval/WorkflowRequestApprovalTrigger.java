package com.source.rworkflow.workflow.domain.approval;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.domain.approval.assignee.WorkflowRequestApprovalAssignee;
import com.source.rworkflow.workflow.domain.approval.assignee.WorkflowRequestApprovalAssigneeCompositeService;
import com.source.rworkflow.workflow.domain.executeAssignee.WorkflowRequestExecutionAssigneeCompositeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestApprovalTrigger {
    private final WorkflowRequestApprovalAssigneeCompositeService approvalAssigneeCompositeService;
    private final WorkflowRequestExecutionAssigneeCompositeService executionAssigneeCompositeService;

    public WorkflowRequestApprovalAssignee afterUrgentCreate(final Long requestId, final Long approvalId){
        return approvalAssigneeCompositeService.createUrgent(requestId, approvalId);
    }
    public List<WorkflowRequestApprovalAssignee> afterCreate(final List<Long> assigneeIds, final Long requestId, final Long approvalId) {
        return approvalAssigneeCompositeService.createCollection(assigneeIds, requestId, approvalId);
    }

    public boolean beforeApprove(final WorkflowRequestApproval approval, final SessionUserId sessionUserId, final boolean approve) {
        return approvalAssigneeCompositeService.approve(approval, sessionUserId, approve);
    }

    public void afterApproveExecutionPending(final Long requestId, final SessionUserId sessionUserId) {
        executionAssigneeCompositeService.setAssigneesPending(requestId, sessionUserId);
    }
}
