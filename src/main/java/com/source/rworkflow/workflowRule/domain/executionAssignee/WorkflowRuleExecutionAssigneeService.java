package com.source.rworkflow.workflowRule.domain.executionAssignee;

import com.source.rworkflow.workflowRule.domain.approvalAssignee.WorkflowRuleApprovalAssignee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRuleExecutionAssigneeService {
    private final WorkflowRuleExecutionAssigneeRepository repository;

    public WorkflowRuleExecutionAssignee create(final WorkflowRuleExecutionAssignee request) {
        return repository.save(request);
    }

    public List<WorkflowRuleExecutionAssignee> find(final Long approvalId) {
        return repository.findAllByRuleApprovalId(approvalId);
    }
}
