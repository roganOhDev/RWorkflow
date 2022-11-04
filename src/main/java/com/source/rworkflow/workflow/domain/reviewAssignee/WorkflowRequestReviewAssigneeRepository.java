package com.source.rworkflow.workflow.domain.reviewAssignee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRequestReviewAssigneeRepository extends JpaRepository<WorkflowRequestReviewAssignee, Long> {
}
