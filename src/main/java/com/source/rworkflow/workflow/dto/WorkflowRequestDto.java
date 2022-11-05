package com.source.rworkflow.workflow.dto;

import com.source.rworkflow.misc.user.UserDto;
import com.source.rworkflow.workflow.domain.approval.WorkflowRequestApproval;
import com.source.rworkflow.workflow.domain.request.WorkflowRequest;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkflowRequestDto {
    @Getter
    public static class Create{
        @Getter
        public static class Request{
            @NotNull(message = "Must Have Title")
            private String title;
            @NotNull(message = "Must Have WorkflowRequestType")
            private WorkflowRequestType type;
            private Long ruleId;
            @NotNull(message = "Must Have If This is Urgent")
            private boolean urgent;
            @NotNull(message = "Must Have Coment")
            @Size(min = 1, max = 256)
            private String comment;

            @Valid
            @Size()
            private List<WorkflowApprovalDto.Create.Request> approvals;
            @Valid
            @Size(min = 1)
            private List<Long> executionAssignees;
            @Valid
            @Size(min = 1)
            private List<Long> reviewAssignees;

            @Valid
            @NotNull
            private Detail detail;

            @Getter
            public static class Detail{
                private LocalDateTime executionExpiryAt;
                private LocalDateTime requestExpiryAt;
                @Valid
                private AccessControlDto.Request accessControl;
                @Valid
                private List<SqlExecutionDto.Request> sqlExecutions;
                @Valid
                private List<DataExportDto.Request> dataExports;

                public void setDefaultExecutionExpiryAt() {
                    this.executionExpiryAt = LocalDateTime.now().plusDays(4);
                }

                public void setDefaultRequestExpiryAt() {
                    this.requestExpiryAt = LocalDateTime.now().plusDays(5);
                }
            }
        }

        @Getter
        public static class Response{
            private Long id;
            private String title;
            private WorkflowRequestType type;
            private Long ruleId;
            private boolean urgent;
            private String comment;
            private List<WorkflowApprovalDto.Create.Response> approvals;
            private List<UserDto> executionAssignees;
            private List<UserDto> reviewAssignees;

            public static Response from(final WorkflowRequest workflowRequest, final List<WorkflowRequestApproval> approvals,
                                        final Map<Long, List<UserDto>> approvalAssignees, final List<UserDto> executionAssignees,
                                        final List<UserDto> reviewAssignees) {
                final var response =new Response();

                response.id = workflowRequest.getId();
                response.title = workflowRequest.getTitle();
                response.type = workflowRequest.getType();
                response.ruleId = workflowRequest.getRuleId();
                response.urgent = workflowRequest.isUrgent();
                response.comment = workflowRequest.getComment();
                response.approvals = approvals(approvals, approvalAssignees);
                response.executionAssignees = executionAssignees;
                response.reviewAssignees = reviewAssignees;

                return response;
            }

            private static List<WorkflowApprovalDto.Create.Response> approvals(final List<WorkflowRequestApproval> approvals, final Map<Long, List<UserDto>> approvalAssignee) {
                return approvals.stream()
                        .map(approval -> WorkflowApprovalDto.Create.Response.from(approval, approvalAssignee.get(approval.getId())))
                        .collect(Collectors.toUnmodifiableList());
            }
        }
    }
}
