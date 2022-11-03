package com.source.rworkflow.workflowRule.domain.rule;

import com.source.rworkflow.workflow.type.WorkflowRequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRuleRepository extends JpaRepository<WorkflowRule, Long> {
    List<WorkflowRule> findAllByRequestTypeAndDeletedIsFalse(WorkflowRequestType requestType);

    WorkflowRule findByIdAndDeletedIsFalse(Long id);

}
