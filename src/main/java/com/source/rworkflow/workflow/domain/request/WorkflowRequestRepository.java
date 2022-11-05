package com.source.rworkflow.workflow.domain.request;

import com.source.rworkflow.workflow.type.WorkflowRequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRequestRepository extends JpaRepository<WorkflowRequest, Long> {
    List<WorkflowRequest> findAllByRuleId(Long ruleId);
    List<WorkflowRequest> findAllByType(WorkflowRequestType type);
}
