package com.source.rworkflow.workflowRule.domain.executionAssignee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRuleExecutionAssigneeRepository extends JpaRepository<WorkflowRuleExecutionAssignee, Long> {
    List<WorkflowRuleExecutionAssignee> findAllByRuleId(final Long approvalId);
}
