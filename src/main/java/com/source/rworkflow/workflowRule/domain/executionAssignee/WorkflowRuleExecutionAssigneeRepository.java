package com.source.rworkflow.workflowRule.domain.executionAssignee;

import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRuleExecutionAssigneeRepository extends JpaRepository<WorkflowRuleExecutionAssignee, Long> {
}
