package com.source.rworkflow.workflow.domain.request.sqlExecution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRequestDetailSqlExecutionRepository extends JpaRepository<WorkflowRequestDetailSqlExecution, Long> {
    WorkflowRequestDetailSqlExecution findByRequestId(Long requestId);
}
