package com.source.rworkflow.workflow.domain.request.sqlExecution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRequestDetailSqlExecutionRepository extends JpaRepository<WorkflowRequestDetailSqlExecution, Long> {
    List<WorkflowRequestDetailSqlExecution> findAllByRequestId(Long requestId);
}
