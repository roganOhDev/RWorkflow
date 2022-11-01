package com.source.rworkflow.workflowRule.dto;

import com.source.rworkflow.workflowRule.type.ApproveType;
import lombok.Getter;

import java.util.List;

public class WorkflowRuleApprovalDto {
    @Getter
    public static class Request{
        private ApproveType approveType;
        private int order;
        private List<AssigneeDto.Request> assignees;
    }
}
