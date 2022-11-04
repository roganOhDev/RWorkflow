package com.source.rworkflow.workflow.domain.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRequestRepository extends JpaRepository<WorkflowRequest, Long> {
}
