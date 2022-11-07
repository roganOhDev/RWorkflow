package com.source.rworkflow.workflow.domain.approval;

import com.source.rworkflow.workflow.type.ApprovalStatusType;
import com.source.rworkflow.workflowRule.type.ApproveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestApprovalService {
    private final WorkflowRequestApprovalRepository repository;

    public WorkflowRequestApproval create(final WorkflowRequestApproval approval) {
        approval.setStatus(ApprovalStatusType.PENDING);

        return repository.save(approval);
    }

    public WorkflowRequestApproval createUrgent(final WorkflowRequestApproval approval) {
        return repository.save(approval);
    }

    public List<WorkflowRequestApproval> findAll() {
        return repository.findAll();
    }

    public List<WorkflowRequestApproval> findAllByRequestId(final Long requestId) {
        return repository.findAllByRequestId(requestId);
    }

    public WorkflowRequestApproval approve(final WorkflowRequestApproval approval) {
        return repository.save(approval);
    }
}
