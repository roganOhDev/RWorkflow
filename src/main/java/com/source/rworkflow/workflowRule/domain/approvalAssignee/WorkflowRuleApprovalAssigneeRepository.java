package com.source.rworkflow.workflowRule.domain.approvalAssignee;

import com.source.rworkflow.workflowRule.domain.approvalAssignee.WorkflowRuleApprovalAssignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRuleApprovalAssigneeRepository extends JpaRepository<WorkflowRuleApprovalAssignee,Long> {
}
