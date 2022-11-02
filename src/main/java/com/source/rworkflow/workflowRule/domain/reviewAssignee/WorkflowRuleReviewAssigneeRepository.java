package com.source.rworkflow.workflowRule.domain.reviewAssignee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRuleReviewAssigneeRepository extends JpaRepository<WorkflowRuleReviewAssignee, Long> {
    List<WorkflowRuleReviewAssignee> findAllByRuleId(Long ruleId);
}
