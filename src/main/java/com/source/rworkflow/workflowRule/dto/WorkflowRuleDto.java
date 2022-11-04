package com.source.rworkflow.workflowRule.dto;

import com.source.rworkflow.workflow.type.WorkflowRequestType;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
import com.source.rworkflow.workflowRule.domain.approval.WorkflowRuleApproval;
import com.source.rworkflow.workflowRule.domain.approval.assignee.WorkflowRuleApprovalAssignee;
import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssignee;
import com.source.rworkflow.workflowRule.domain.reviewAssignee.WorkflowRuleReviewAssignee;
import com.source.rworkflow.workflowRule.domain.rule.WorkflowRule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkflowRuleDto {
    @Getter
    public static class Create {
        @Getter
        public static class Request {
            @NotNull(message = "need name")
            private String name;
            @NotNull(message = "need type")
            private WorkflowRequestType type;
            @NotNull(message = "need urgent")
            private Boolean urgent;
            @Valid
            private List<WorkflowRuleApprovalDto.Request> approvals;
            @Valid
            private List<AssigneeDto.Request> executionAssignees;
            @Valid
            private List<AssigneeDto.Request> reviewAssignees;

            public boolean isUrgent() {
                return this.urgent;
            }
        }

    }

    @Getter
    public static class Update {
        @Getter
        public static class EntityMapperDto {
            private Long id;
            private String name;
            private Boolean urgent;

            public static EntityMapperDto from(final Request request) {
                final var dto = new EntityMapperDto();

                dto.id = request.id;
                dto.name = request.name;
                dto.urgent = request.urgent;

                return dto;
            }
        }

        @Getter
        public static class Request {
            @NotNull
            private Long id;
            private String name;
            private Boolean urgent;
            private List<WorkflowRuleApprovalDto.Request> approvals;
            private List<AssigneeDto.Request> executionAssignees;
            private List<AssigneeDto.Request> reviewAssignees;
        }
    }

    @Getter
    public static class Delete {
        @Getter
        @AllArgsConstructor
        public static class Response {
            private Long id;
            private String name;
            private WorkflowRequestType workflowType;
        }
    }

    @Getter
    public static class Response {
        private Long id;
        private String name;
        private WorkflowRequestType type;
        private boolean urgent;
        private List<WorkflowRuleApprovalDto.Response> approvals;
        private List<AssigneeDto.Response> executionAssignees;
        private List<AssigneeDto.Response> reviewAssignees;

        public static Response from(final WorkflowRule workflowRule, final WorkflowRuleSuite ruleSuite) {
            final var response = new Response();

            response.id = workflowRule.getId();
            response.name = workflowRule.getName();
            response.type = workflowRule.getRequestType();
            response.urgent = workflowRule.isUrgent();

            response.approvals = approvals(ruleSuite.getApprovals(), ruleSuite.getApprovalAssignees());
            response.executionAssignees = ruleSuite.getExecutionAssignees().stream()
                    .map(AssigneeDto.Response::from)
                    .collect(Collectors.toUnmodifiableList());
            response.reviewAssignees = ruleSuite.getReviewAssignees().stream()
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

    @Getter
    public static class Read {

        @Getter
        public static class Response {
            Long count;
            private List<WorkflowRuleDto.Response> rules;

            public Response(List<WorkflowRuleDto.Response> rules) {
                this.count = (long) rules.size();
                this.rules = rules;
            }
        }
    }
}
