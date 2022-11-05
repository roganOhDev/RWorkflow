package com.source.rworkflow.workflow.domain.request.accessControl.connection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRequestDetailAccessControlConnectionRepository extends JpaRepository<WorkflowRequestDetailAccessControlConnection, Long> {
}
