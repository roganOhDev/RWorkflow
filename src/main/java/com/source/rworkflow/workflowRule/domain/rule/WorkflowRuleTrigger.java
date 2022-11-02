package com.source.rworkflow.workflowRule.domain.rule;

import com.source.rworkflow.workflowRule.domain.approval.WorkflowRuleApprovalCompositeService;
import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssigneeCompositeService;
import com.source.rworkflow.workflowRule.domain.reviewAssignee.WorkflowRuleReviewAssigneeCompositeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRuleTrigger {
    private final WorkflowRuleApprovalCompositeService workflowRuleApprovalCompositeService;
    private final WorkflowRuleExecutionAssigneeCompositeService workflowRuleExecutionAssigneeCompositeService;
    private final WorkflowRuleReviewAssigneeCompositeService workflowRuleReviewAssigneeCompositeService;

    public void beforeDelete(final WorkflowRule workflowRule) {
        final var ruleId = workflowRule.getId();

        workflowRuleApprovalCompositeService.deleteCollection(ruleId);

        final var executionAssignees = workflowRuleExecutionAssigneeCompositeService.findAllByRuleId(ruleId);
        workflowRuleExecutionAssigneeCompositeService.deleteCollection(executionAssignees);

        final var reviewAssignees = workflowRuleReviewAssigneeCompositeService.findAllByRuleId(ruleId);
        workflowRuleReviewAssigneeCompositeService.deleteCollection(reviewAssignees);
    }
}
