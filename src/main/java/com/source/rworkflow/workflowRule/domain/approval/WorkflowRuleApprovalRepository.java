package com.source.rworkflow.workflowRule.domain.approval;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRuleApprovalRepository extends JpaRepository<WorkflowRuleApproval, Long> {
}
