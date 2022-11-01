package com.source.rworkflow.workflowRule.domain.rule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRuleService {
    private final WorkflowRuleRepository repository;
}
