package com.source.rworkflow.workflowRule.dto;

import com.source.rworkflow.workflowRule.type.AssigneeType;
import lombok.Getter;

public class AssigneeDto {
    @Getter
    public static class Request{
        private AssigneeType type;
        private Long id;
    }
}
