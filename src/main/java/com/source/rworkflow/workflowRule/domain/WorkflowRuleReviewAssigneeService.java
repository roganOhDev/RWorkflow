package com.source.rworkflow.workflowRule.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRuleReviewAssigneeService {
    private final WorkflowRuleReviewAssigneeRepository repository;
}
