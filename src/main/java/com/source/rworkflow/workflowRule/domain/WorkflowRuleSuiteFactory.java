package com.source.rworkflow.workflowRule.domain;

import com.source.rworkflow.workflowRule.domain.approval.WorkflowRuleApprovalCompositeService;
import com.source.rworkflow.workflowRule.domain.approval.assignee.WorkflowRuleApprovalAssigneeCompositeService;
import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssigneeCompositeService;
import com.source.rworkflow.workflowRule.domain.reviewAssignee.WorkflowRuleReviewAssigneeCompositeService;
import com.source.rworkflow.workflowRule.domain.rule.WorkflowRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRuleSuiteFactory {
    private final WorkflowRuleApprovalCompositeService workflowRuleApprovalCompositeService;
    private final WorkflowRuleApprovalAssigneeCompositeService workflowRuleApprovalAssigneeCompositeService;
    private final WorkflowRuleExecutionAssigneeCompositeService workflowRuleExecutionAssigneeCompositeService;
    private final WorkflowRuleReviewAssigneeCompositeService workflowRuleReviewAssigneeCompositeService;


    public WorkflowRuleSuite of(final WorkflowRule rule) {
        final var ruleSuite = new WorkflowRuleSuite();

        ruleSuite.setApprovals(workflowRuleApprovalCompositeService.findAllByRuleId(rule.getId()).stream()
                .peek(approval -> ruleSuite.putApprovalAssignees(new HashMap<>(Map.of(approval.getId(), workflowRuleApprovalAssigneeCompositeService.find(approval.getId())))))
                .collect(Collectors.toUnmodifiableList()));

        ruleSuite.setExecutionAssignees(workflowRuleExecutionAssigneeCompositeService.findAllByRuleId(rule.getId()));

        ruleSuite.setReviewAssignees(workflowRuleReviewAssigneeCompositeService.findAllByRuleId(rule.getId()));

        return ruleSuite;

    }

}