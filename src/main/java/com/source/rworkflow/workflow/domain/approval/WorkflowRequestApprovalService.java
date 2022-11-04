package com.source.rworkflow.workflow.domain.approval;

import com.source.rworkflow.workflow.type.ApprovalStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestApprovalService {
    private final WorkflowRequestApprovalRepository repository;

    public WorkflowRequestApproval create(final WorkflowRequestApproval approval) {
        approval.setStatus(ApprovalStatusType.PENDING);

        return repository.save(approval);
    }
}
