package com.source.rworkflow.workflow.domain.approval.assignee;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.type.AssigneeStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestApprovalAssigneeService {
    private final WorkflowRequestApprovalAssigneeRepository repository;

    public WorkflowRequestApprovalAssignee create(final WorkflowRequestApprovalAssignee assignee) {
        assignee.setStatus(AssigneeStatusType.PENDING);

        return repository.save(assignee);
    }

    public List<WorkflowRequestApprovalAssignee> findAllByApprovalId(final Long approvalId) {
        return repository.findAllByRequestApprovalId(approvalId);
    }

    public List<WorkflowRequestApprovalAssignee> findAll() {
        return repository.findAll();
    }

    public WorkflowRequestApprovalAssignee approve(final WorkflowRequestApprovalAssignee assignee, final SessionUserId sessionUserId, final boolean approve) {
        if (approve) {
            assignee.setStatus(AssigneeStatusType.APPROVED);
        } else {
            assignee.setStatus(AssigneeStatusType.REJECTED);
        }

        assignee.setActionAt(LocalDateTime.now());
        assignee.setActionBy(sessionUserId.getId());

        return repository.save(assignee);
    }
}
