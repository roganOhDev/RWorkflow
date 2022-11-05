package com.source.rworkflow.workflow.dto;

import com.source.rworkflow.misc.user.UserDto;
import com.source.rworkflow.workflow.domain.approval.WorkflowRequestApproval;
import com.source.rworkflow.workflowRule.type.ApproveType;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class WorkflowApprovalDto {
    @Getter
    public static class Create{
        @Getter
        public static class Request {

            @NotNull(message = "Must Have Order")
            @Max(3)
            @Min(1)
            private Long order;

            @NotNull(message = "Must Have ApproveType")
            private ApproveType approveType;

            @Size(min = 1)
            private List<Long> assignees;
        }

        @Getter
        public static class Response {
            private Long order;
            private ApproveType approveType;
            private List<UserDto> assignees;

            public static Response from(final WorkflowRequestApproval approval, List<UserDto> approvalAssignees) {
                final var response = new Response();

                response.order = approval.getOrder();
                response.approveType = approval.getApproveType();
                response.assignees = approvalAssignees;

                return response;
            }
        }
    }
}
