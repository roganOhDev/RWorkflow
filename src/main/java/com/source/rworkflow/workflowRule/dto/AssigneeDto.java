package com.source.rworkflow.workflowRule.dto;

import com.source.rworkflow.workflowRule.domain.approvalAssignee.WorkflowRuleApprovalAssignee;
import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssignee;
import com.source.rworkflow.workflowRule.domain.reviewAssignee.WorkflowRuleReviewAssignee;
import com.source.rworkflow.workflowRule.type.AssigneeType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public class AssigneeDto {
    @Getter
    @EqualsAndHashCode()
    public static class Request{
        @NotNull
        private AssigneeType type;
        @NotNull
        private Long value;
    }

    @Getter
    public static class Response{
        private Long id;
        private AssigneeType type;
        private Long value;

        public static Response from(final WorkflowRuleApprovalAssignee assignee) {
            final var response = new Response();

            response.id = assignee.getId();
            response.type = assignee.getAssigneeType();
            response.value = assignee.getAssigneeValue();

            return response;
        }

        public static Response from(final WorkflowRuleExecutionAssignee assignee) {
            final var response = new Response();

            response.id = assignee.getId();
            response.type = assignee.getAssigneeType();
            response.value = assignee.getAssigneeValue();

            return response;
        }

        public static Response from(final WorkflowRuleReviewAssignee assignee) {
            final var response = new Response();

            response.id = assignee.getId();
            response.type = assignee.getAssigneeType();
            response.value = assignee.getAssigneeValue();

            return response;
        }
    }
}
