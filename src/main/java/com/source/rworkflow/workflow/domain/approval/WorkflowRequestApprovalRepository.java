package com.source.rworkflow.workflow.domain.approval;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRequestApprovalRepository extends JpaRepository<WorkflowRequestApproval, Long> {
}
