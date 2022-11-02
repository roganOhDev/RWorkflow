package com.source.rworkflow.workflowRule.domain.rule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRuleRepository extends JpaRepository<WorkflowRule, Long> {
}
