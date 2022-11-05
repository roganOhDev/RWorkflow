package com.source.rworkflow.workflow.domain.request.accessControl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRequestDetailAccessControlRepository extends JpaRepository<WorkflowRequestDetailAccessControl, Long> {
}
