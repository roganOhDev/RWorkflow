package com.source.rworkflow.workflow.domain.approval.assignee;

import com.source.rworkflow.workflow.type.AssigneeStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
