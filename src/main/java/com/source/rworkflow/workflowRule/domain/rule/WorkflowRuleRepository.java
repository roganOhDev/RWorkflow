package com.source.rworkflow.workflowRule.domain.rule;

import com.source.rworkflow.workflowRule.domain.rule.WorkflowRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRuleRepository extends JpaRepository<WorkflowRule, Long> {
}
