package com.source.rworkflow.workflowRule.domain.rule;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflowRule.domain.approval.WorkflowRuleApprovalCompositeService;
import com.source.rworkflow.workflowRule.domain.approvalAssignee.WorkflowRuleApprovalAssignee;
import com.source.rworkflow.workflowRule.domain.approvalAssignee.WorkflowRuleApprovalAssigneeCompositeService;
import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssigneeCompositeService;
import com.source.rworkflow.workflowRule.domain.reviewAssignee.WorkflowRuleReviewAssigneeCompositeService;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRuleTransferService {
    private final WorkflowRuleCompositeService compositeService;
    private final WorkflowRuleApprovalCompositeService workflowRuleApprovalCompositeService;
    private final WorkflowRuleApprovalAssigneeCompositeService workflowRuleApprovalAssigneeCompositeService;
    private final WorkflowRuleExecutionAssigneeCompositeService workflowRuleExecutionAssigneeCompositeService;
    private final WorkflowRuleReviewAssigneeCompositeService workflowRuleReviewAssigneeCompositeService;

    public WorkflowRuleDto.Create.Response create(final WorkflowRuleDto.Create.Request request, final SessionUserId sessionUserId) {
        final var created = compositeService.create(request, sessionUserId);

        final var createdApprovals = workflowRuleApprovalCompositeService.createCollection(created.getId(), request.getApprovals());

        final var createdApprovalAssignees = new HashMap<Long, List<WorkflowRuleApprovalAssignee>>();
        createdApprovals
                .forEach(approval -> {
                    final var assignees = workflowRuleApprovalAssigneeCompositeService.find(approval.getId());
                    createdApprovalAssignees.put(approval.getId(), assignees);
                });

        final var createdExecutions = workflowRuleExecutionAssigneeCompositeService.createCollection(created.getId(), request.getExecutions());
        final var createdReviews = workflowRuleReviewAssigneeCompositeService.createCollection(created.getId(), request.getExecutions());

        return WorkflowRuleDto.Create.Response.from(created, createdApprovals, createdApprovalAssignees, createdExecutions, createdReviews);
    }
}
