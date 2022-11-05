package com.source.rworkflow.workflow.domain.request.accessControl.connection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRequestDetailAccessControlConnectionRepository extends JpaRepository<WorkflowRequestDetailAccessControlConnection, Long> {
    List<WorkflowRequestDetailAccessControlConnection> findAllByDetailAccessControlId(Long accessControlId);
}
