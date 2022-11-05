package com.source.rworkflow.workflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.source.rworkflow.common.util.DateFormat;
import com.source.rworkflow.misc.user.UserDto;
import com.source.rworkflow.workflow.domain.approval.WorkflowRequestApproval;
import com.source.rworkflow.workflow.domain.request.WorkflowRequest;
import com.source.rworkflow.workflow.domain.request.accessControl.WorkflowRequestDetailAccessControl;
import com.source.rworkflow.workflow.domain.request.accessControl.connection.WorkflowRequestDetailAccessControlConnection;
import com.source.rworkflow.workflow.domain.request.dataExport.WorkflowRequestDetailDataExport;
import com.source.rworkflow.workflow.domain.request.sqlExecution.WorkflowRequestDetailSqlExecution;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import lombok.AllArgsConstructor;
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
    public static class Create {
        @Getter
        public static class Request {
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
            @Size(min = 1)
            private List<Long> executionAssignees;
            @Size(min = 1)
            private List<Long> reviewAssignees;

            @Valid
            @NotNull(message = "Must Have detail")
            private Detail detail;

            @Getter
            public static class Detail {
                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormat.FORMAT, timezone = "Asia/Seoul")
                private LocalDateTime executionExpiryAt;
                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormat.FORMAT, timezone = "Asia/Seoul")
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
    }

    @Getter
    public static class Response {
        protected Long id;
        protected String title;
        protected WorkflowRequestType type;
        protected Long ruleId;
        protected boolean urgent;
        protected String comment;
        protected List<WorkflowApprovalDto.Create.Response> approvals;
        protected List<UserDto> executionAssignees;
        protected List<UserDto> reviewAssignees;

        public Response(WorkflowRequest workflowRequest, List<WorkflowRequestApproval> approvals, Map<Long, ? extends List<UserDto>> approvalAssignees, List<UserDto> executionAssignees, List<UserDto> reviewAssignees) {
            this.id = workflowRequest.getId();
            this.title = workflowRequest.getTitle();
            this.type = workflowRequest.getType();
            this.ruleId = workflowRequest.getRuleId();
            this.urgent = workflowRequest.isUrgent();
            this.comment = workflowRequest.getComment();
            this.approvals = approvals(approvals, approvalAssignees);
            this.executionAssignees = executionAssignees;
            this.reviewAssignees = reviewAssignees;
        }

        protected static List<WorkflowApprovalDto.Create.Response> approvals(final List<WorkflowRequestApproval> approvals, final Map<Long, ? extends List<UserDto>> approvalAssignee) {
            return approvals.stream()
                    .map(approval -> WorkflowApprovalDto.Create.Response.from(approval, approvalAssignee.get(approval.getId())))
                    .collect(Collectors.toUnmodifiableList());
        }
    }

    @Getter
    public static class DetailResponse extends Response {
        private Detail detail;

        @Getter
        @AllArgsConstructor
        public static class Detail {
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormat.FORMAT, timezone = "Asia/Seoul")
            private LocalDateTime executionExpiryAt;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormat.FORMAT, timezone = "Asia/Seoul")
            private LocalDateTime requestExpiryAt;
            private List<AccessControlConnectionDto.Response> accessControl;
            private List<SqlExecutionDto.Response> sqlExecutions;
            private List<DataExportDto.Response> dataExports;

            public Detail(WorkflowRequest request, WorkflowRequestDetailAccessControl accessControl,
                          List<WorkflowRequestDetailAccessControlConnection> accessControlConnections, List<WorkflowRequestDetailSqlExecution> sqlExecutions,
                          List<WorkflowRequestDetailDataExport> dataExecutions) {

                final var type = request.getType();

                switch (type) {
                    case ACCESS_CONTROL:
                        setExpiryAt(this, null, accessControl.getRequestExpiryAt());
                        this.accessControl = accessControlConnections.stream()
                                .map(AccessControlConnectionDto.Response::from)
                                .collect(Collectors.toUnmodifiableList());
                        break;
                    case SQL_EXECUTION:
                        setExpiryAt(this, sqlExecutions.get(0).getExecutionExpiryAt(), sqlExecutions.get(0).getRequestExpiryAt());
                        this.sqlExecutions = sqlExecutions.stream()
                                .map(SqlExecutionDto.Response::from)
                                .collect(Collectors.toUnmodifiableList());
                        break;
                    case DATA_EXPORT:
                        setExpiryAt(this,dataExecutions.get(0).getExecutionExpiryAt(), dataExecutions.get(0).getRequestExpiryAt());
                        this.dataExports = dataExecutions.stream()
                                .map(DataExportDto.Response::from)
                                .collect(Collectors.toUnmodifiableList());
                        break;
                }
            }

            private void setExpiryAt(Detail detail, final LocalDateTime executionExpiryAt, final LocalDateTime requestExpiryAt) {
                detail.executionExpiryAt = executionExpiryAt;
                detail.requestExpiryAt = requestExpiryAt;
            }
        }

        public DetailResponse(final WorkflowRequest workflowRequest, final List<WorkflowRequestApproval> approvals,
                              final Map<Long, ? extends List<UserDto>> approvalAssignees, final List<UserDto> executionAssignees,
                              final List<UserDto> reviewAssignees, final WorkflowRequestDetailAccessControl detailAccessControl,
                              final List<WorkflowRequestDetailAccessControlConnection> detailAccessControlConnections, final List<WorkflowRequestDetailSqlExecution> detailSqlExecutions,
                              final List<WorkflowRequestDetailDataExport> detailDataExecutions) {

            super(workflowRequest, approvals, approvalAssignees, executionAssignees, reviewAssignees);

            this.detail = new Detail(workflowRequest, detailAccessControl, detailAccessControlConnections, detailSqlExecutions, detailDataExecutions);
        }
    }


}
