package com.source.rworkflow.workflowRule.dto;

import com.source.rworkflow.workflow.type.WorkflowRequestType;
import com.source.rworkflow.workflowRule.domain.approval.WorkflowRuleApproval;
import com.source.rworkflow.workflowRule.domain.approvalAssignee.WorkflowRuleApprovalAssignee;
import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssignee;
import com.source.rworkflow.workflowRule.domain.reviewAssignee.WorkflowRuleReviewAssignee;
import com.source.rworkflow.workflowRule.domain.rule.WorkflowRule;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkflowRuleDto {
    @Getter
    public static class Create {
        @Getter
        public static class Request {
            @NotNull
            private String name;
            @NotNull
            private WorkflowRequestType type;
            private boolean urgent;
            private List<WorkflowRuleApprovalDto.Request> approvals;
            private List<AssigneeDto.Request> executions;
            private List<AssigneeDto.Request> reviews;
        }

        @Getter
        @Setter
        public static class Response {
            private String name;
            private WorkflowRequestType type;
            private boolean urgent;
            private List<WorkflowRuleApprovalDto.Response> approvals;
            private List<AssigneeDto.Response> executions;
            private List<AssigneeDto.Response> reviews;

            public static Response from(final WorkflowRule workflowRule, final List<WorkflowRuleApproval> workflowRuleApprovals,
                                        final Map<Long, List<WorkflowRuleApprovalAssignee>> workflowRuleApprovalAssignees,
                                        final List<WorkflowRuleExecutionAssignee> workflowRuleExecutionAssignees,
                                        final List<WorkflowRuleReviewAssignee> workflowRuleReviewAssignees) {
                final var response = new Response();

                response.name = workflowRule.getName();
                response.type = workflowRule.getRequestType();
                response.urgent = workflowRule.isUrgent();

                response.approvals = approvals(workflowRuleApprovals, workflowRuleApprovalAssignees);
                response.executions = workflowRuleExecutionAssignees.stream()
                        .map(AssigneeDto.Response::from)
                        .collect(Collectors.toUnmodifiableList());
                response.reviews = workflowRuleReviewAssignees.stream()
                        .map(AssigneeDto.Response::from)
                        .collect(Collectors.toUnmodifiableList());

                return response;
            }

            private static List<WorkflowRuleApprovalDto.Response> approvals(final List<WorkflowRuleApproval> workflowRuleApprovals,
                                                                            final Map<Long, List<WorkflowRuleApprovalAssignee>> workflowRuleApprovalAssignees) {
                return workflowRuleApprovals.stream()
                        .map(approval -> {
                            final var assignees = workflowRuleApprovalAssignees.get(approval.getId());
                            return WorkflowRuleApprovalDto.Response.from(approval, assignees);
                        })
                        .collect(Collectors.toUnmodifiableList());
            }
        }
    }
}
