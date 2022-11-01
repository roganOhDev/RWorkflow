package com.source.rworkflow.workflowRule.dto;

import com.source.rworkflow.workflow.type.WorkflowRequestType;
import lombok.Getter;

import java.util.List;

public class WorkflowRuleDto {
    @Getter
    public static class Create{
        @Getter
        public static class Request{
            private String name;
            private WorkflowRequestType type;
            private boolean urgent;
            private List<WorkflowRuleApprovalDto.Request> approval;
            private List<AssigneeDto> execution;
            private List<AssigneeDto> review;

        }

        public static class Response{
        }
    }
}
