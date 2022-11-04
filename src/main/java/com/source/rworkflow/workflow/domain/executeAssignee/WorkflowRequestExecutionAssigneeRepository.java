package com.source.rworkflow.workflow.domain.executeAssignee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRequestExecutionAssigneeRepository extends JpaRepository<WorkflowRequestExecutionAssignee, Long> {
}
