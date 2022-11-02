package com.source.rworkflow.workflowRule.domain.approval;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRuleApprovalService {
    private final WorkflowRuleApprovalRepository repository;

    public WorkflowRuleApproval create(final WorkflowRuleApproval request) {
        return repository.save(request);
    }
}
