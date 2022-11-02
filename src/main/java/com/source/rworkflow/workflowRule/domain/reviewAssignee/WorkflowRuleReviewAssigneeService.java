package com.source.rworkflow.workflowRule.domain.reviewAssignee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRuleReviewAssigneeService {
    private final WorkflowRuleReviewAssigneeRepository repository;

    public WorkflowRuleReviewAssignee create(final WorkflowRuleReviewAssignee request) {
        return repository.save(request);
    }

    public List<WorkflowRuleReviewAssignee> findAllByRuleId(final Long ruleId) {
        return repository.findAllByRuleId(ruleId);
    }

    public void delete(final WorkflowRuleReviewAssignee assignee) {
        repository.delete(assignee);
    }
}
