package com.source.rworkflow.workflowRule.domain.approval;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRuleApprovalRepository extends JpaRepository<WorkflowRuleApproval, Long> {
    List<WorkflowRuleApproval> findAllByRuleId(Long ruleId);
}
