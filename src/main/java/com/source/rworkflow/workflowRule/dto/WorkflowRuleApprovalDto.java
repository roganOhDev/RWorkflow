package com.source.rworkflow.workflowRule.dto;

import com.source.rworkflow.workflowRule.domain.approval.WorkflowRuleApproval;
import com.source.rworkflow.workflowRule.domain.approval.assignee.WorkflowRuleApprovalAssignee;
import com.source.rworkflow.workflowRule.type.ApproveType;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

public class WorkflowRuleApprovalDto {
    @Getter
    public static class Request {
        private Long id;
        @NotNull(message = "must have approveType")
        private ApproveType approveType;
        @NotNull(message = "must have order")
        @Min(1)
        @Max(3)
        private Long order;
        @Valid
        @NotNull(message = "must have assignees")
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
