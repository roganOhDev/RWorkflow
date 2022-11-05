package com.source.rworkflow.workflow.domain.request.accessControl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRequestDetailAccessControlRepository extends JpaRepository<WorkflowRequestDetailAccessControl, Long> {
    WorkflowRequestDetailAccessControl findByRequestId(Long requestId);
}
