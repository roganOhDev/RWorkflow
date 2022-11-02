package com.source.rworkflow.workflowRule.domain.approval;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRuleApprovalService {
    private final WorkflowRuleApprovalRepository repository;

    public WorkflowRuleApproval create(final WorkflowRuleApproval request) {
        return repository.save(request);
    }

    public List<WorkflowRuleApproval> findAllByRuleId(final Long ruleId) {
        return repository.findAllByRuleId(ruleId);
    }

    public void delete(final WorkflowRuleApproval approval) {
        repository.delete(approval);
    }
}
