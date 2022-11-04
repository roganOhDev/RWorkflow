package com.source.rworkflow.workflow.dto;

import com.source.rworkflow.workflowRule.type.ApproveType;
import lombok.Getter;

import javax.validation.Valid;
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

            @NotNull
            @Max(3)
            @Min(1)
            private Long order;

            @NotNull
            private ApproveType approveType;

            @Valid
            @Size(min = 1)
            private List<WorkflowApprovalAssigneeDto.Create.Request> assignees;

        }
    }
}
