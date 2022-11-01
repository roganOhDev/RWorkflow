package com.source.rworkflow.workflowRule.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRuleApprovalAssigneeRepository extends JpaRepository<WorkflowRuleApprovalAssignee,Long> {
}
