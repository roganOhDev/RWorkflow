package com.source.rworkflow.workflowRule.domain.approvalAssignee;

import com.source.rworkflow.workflowRule.domain.approvalAssignee.WorkflowRuleApprovalAssigneeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRuleApprovalAssigneeService {
    private final WorkflowRuleApprovalAssigneeRepository repository;
}
