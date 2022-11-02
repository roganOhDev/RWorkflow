package com.source.rworkflow.workflowRule.domain.rule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WorkflowRuleService {
    private final WorkflowRuleRepository repository;

    public WorkflowRule create(final WorkflowRule workflowRule) {
        final var now = LocalDateTime.now();

        workflowRule.setCreatedAt(now);
        workflowRule.setUpdatedAt(now);

        repository.save(workflowRule);

        return repository.save(workflowRule);
    }
}
