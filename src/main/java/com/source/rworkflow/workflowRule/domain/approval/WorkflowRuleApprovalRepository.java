package com.source.rworkflow.workflowRule.domain.approval;

import com.source.rworkflow.workflowRule.domain.approval.WorkflowRuleApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRuleApprovalRepository extends JpaRepository<WorkflowRuleApproval, Long> {
}
