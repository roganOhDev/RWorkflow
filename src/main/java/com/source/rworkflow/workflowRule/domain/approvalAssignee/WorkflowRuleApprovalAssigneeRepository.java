package com.source.rworkflow.workflowRule.domain.approvalAssignee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRuleApprovalAssigneeRepository extends JpaRepository<WorkflowRuleApprovalAssignee,Long> {
    List<WorkflowRuleApprovalAssignee> findAllByRuleApprovalId(final Long approvalId);
}
