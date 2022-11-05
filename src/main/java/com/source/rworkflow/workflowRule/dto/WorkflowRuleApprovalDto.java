package com.source.rworkflow.workflowRule.dto;

import com.source.rworkflow.workflowRule.domain.approval.WorkflowRuleApproval;
import com.source.rworkflow.workflowRule.domain.approval.assignee.WorkflowRuleApprovalAssignee;
import com.source.rworkflow.workflowRule.type.ApproveType;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class WorkflowRuleApprovalDto {
    @Getter
    public static class Request {
        private Long id;
        @NotNull
        private ApproveType approveType;
        @NotNull
        private Long order;
        @Valid
        @NotNull
        private List<AssigneeDto.Request> assignees;
    }

    @Getter
    public static class Response {
        private Long id;
        private Long order;
        private ApproveType approveType;
        private List<AssigneeDto.Response> assignees;

        public static Response from(final WorkflowRuleApproval approval,
                                    final List<WorkflowRuleApprovalAssignee> approvalAssignees) {
            final var response = new Response();

            response.id = approval.getId();
            response.order = approval.getOrder();
            response.approveType = approval.getApproveType();

            response.assignees = approvalAssignees.stream()
                    .map(AssigneeDto.Response::from)
                    .collect(Collectors.toUnmodifiableList());

            return response;
        }
    }
}
