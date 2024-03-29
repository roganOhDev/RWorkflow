package com.source.rworkflow.workflowRule.domain.approval.assignee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRuleApprovalAssigneeService {
    private final WorkflowRuleApprovalAssigneeRepository repository;

    public WorkflowRuleApprovalAssignee create(final WorkflowRuleApprovalAssignee request) {
        return repository.save(request);
    }

    public List<WorkflowRuleApprovalAssignee> find(final Long approvalId) {
        return repository.findAllByRuleApprovalId(approvalId);
    }

    public void delete(final WorkflowRuleApprovalAssignee assignee) {
        repository.delete(assignee);
    }
}
