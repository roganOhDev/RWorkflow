package com.source.rworkflow.workflow.dto;

import com.source.rworkflow.workflow.type.WorkflowRequestType;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class WorkflowRequestDto {
    @Getter
    public static class Create{
        @Getter
        public static class Request{
            @NotNull
            private String title;
            @NotNull
            private WorkflowRequestType type;
            private Long ruleId;
            @NotNull
            private boolean urgent;
            @NotNull
            @Size(min = 1, max = 256)
            private String comment;
            @Valid
            private List<WorkflowApprovalDto.Create.Request> approvals;
            @Valid
            @Size(min = 1)
            private List<WorkflowExecutionAssigneeDto.Create.Request> executionAssignees;
            @Valid
            @Size(min = 1)
            private List<WorkflowReviewAssigneeDto.Create.Request> reviewAssignees;
        }

    }

    public static class SaveResponse{

    }

}
