package com.source.rworkflow.workflowRule.domain.reviewAssignee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRuleReviewAssigneeService {
    private final WorkflowRuleReviewAssigneeRepository repository;

    public WorkflowRuleReviewAssignee create(final WorkflowRuleReviewAssignee request) {
        return repository.save(request);
    }
}
