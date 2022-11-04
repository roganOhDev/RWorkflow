package com.source.rworkflow.workflowRule.domain;

import com.source.rworkflow.workflowRule.domain.approval.WorkflowRuleApproval;
import com.source.rworkflow.workflowRule.domain.approval.assignee.WorkflowRuleApprovalAssignee;
import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssignee;
import com.source.rworkflow.workflowRule.domain.reviewAssignee.WorkflowRuleReviewAssignee;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class WorkflowRuleSuite {
    private List<WorkflowRuleApproval> approvals;
    private Map<Long, List<WorkflowRuleApprovalAssignee>> approvalAssignees;
    private List<WorkflowRuleExecutionAssignee> executionAssignees;
    private List<WorkflowRuleReviewAssignee> reviewAssignees;

    public WorkflowRuleSuite() {
        approvals = new ArrayList<>();
        approvalAssignees = new HashMap<>();
        executionAssignees = new ArrayList<>();
        reviewAssignees = new ArrayList<>();
    }

    public void setApprovals(List<WorkflowRuleApproval> approvals) {
        this.approvals = approvals;
    }

    public void putApprovalAssignees(HashMap<Long, List<WorkflowRuleApprovalAssignee>> approvalAssignees) {
        this.approvalAssignees.putAll(approvalAssignees);
    }

    public List<Long> findAllApprovalAssigneeByOrder(final Long order) {
        final var ruleApproval = this.approvals.stream()
                .filter(approval -> approval.getOrder().equals(order))
                .findFirst().get();

        return getApprovalAssigneesByApprovalId(ruleApproval.getId()).stream()
                .map(WorkflowRuleApprovalAssignee::getAssigneeValue)
                .collect(Collectors.toUnmodifiableList());
    }

    private List<WorkflowRuleApprovalAssignee> getApprovalAssigneesByApprovalId(final Long approvalId) {
        return this.approvalAssignees.get(approvalId);
    }

    public List<Long> getExecutionAssigneeIds() {
        return this.executionAssignees.stream()
                .map(WorkflowRuleExecutionAssignee::getAssigneeValue)
                .collect(Collectors.toUnmodifiableList());
    }

    public void setExecutionAssignees(List<WorkflowRuleExecutionAssignee> executionAssignees) {
        this.executionAssignees = executionAssignees;
    }

    public void setReviewAssignees(List<WorkflowRuleReviewAssignee> reviewAssignees) {
        this.reviewAssignees = reviewAssignees;
    }
}