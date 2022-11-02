package com.source.rworkflow.workflowRule.domain.approval;

import com.source.rworkflow.workflowRule.domain.approvalAssignee.WorkflowRuleApprovalAssigneeCompositeService;
import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRuleApprovalTrigger {
    private final WorkflowRuleApprovalAssigneeCompositeService approvalAssigneeCompositeService;

    public void afterCreate(final Long approvalId, final List<AssigneeDto.Request> assignees) {
        approvalAssigneeCompositeService.createCollection(approvalId, assignees);
    }
}
