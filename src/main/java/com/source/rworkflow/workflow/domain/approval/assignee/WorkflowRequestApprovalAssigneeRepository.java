package com.source.rworkflow.workflow.domain.approval.assignee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRequestApprovalAssigneeRepository extends JpaRepository<WorkflowRequestApprovalAssignee, Long> {
    List<WorkflowRequestApprovalAssignee>  findAllByRequestApprovalId(Long approvalId);

}
