package com.source.rworkflow.workflowRule.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRuleExecutionAssigneeService {
    private final WorkflowRuleExecutionAssigneeRepository repository;
}
