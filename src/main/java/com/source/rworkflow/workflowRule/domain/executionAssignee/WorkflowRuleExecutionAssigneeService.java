package com.source.rworkflow.workflowRule.domain.executionAssignee;

import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssigneeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRuleExecutionAssigneeService {
    private final WorkflowRuleExecutionAssigneeRepository repository;
}
