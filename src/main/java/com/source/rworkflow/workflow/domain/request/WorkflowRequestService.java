package com.source.rworkflow.workflow.domain.request;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.exception.WorkflowRequestNotFoundException;
import com.source.rworkflow.workflow.type.ApprovalStatusType;
import com.source.rworkflow.workflow.type.ExecutionStatusType;
import com.source.rworkflow.workflow.type.ReviewStatusType;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestService {
    private final WorkflowRequestRepository repository;

    public WorkflowRequest create(final WorkflowRequest workflowRequest, final SessionUserId sessionUserId) {
        final var now = LocalDateTime.now();

        workflowRequest.setApprovalStatus(ApprovalStatusType.PENDING);
        workflowRequest.setExecutionStatus(ExecutionStatusType.NONE);
        workflowRequest.setReviewStatus(ReviewStatusType.NONE);
        workflowRequest.setRequestedAt(now);
        workflowRequest.setRequestedBy(sessionUserId.getId());
        workflowRequest.setExpired(false);
        workflowRequest.setCanceled(false);
        workflowRequest.setCreatedAt(now);
        workflowRequest.setCreatedBy(sessionUserId.getId());
        workflowRequest.setUpdatedAt(now);
        workflowRequest.setUpdatedBy(sessionUserId.getId());

        return repository.save(workflowRequest);
    }

    public List<WorkflowRequest> findByRuleId(final Long ruleId) {
        return repository.findAllByRuleId(ruleId);
    }

    public List<WorkflowRequest> findAllByType(final WorkflowRequestType type) {
        if (type == null) {
            return repository.findAll();
        }

        return repository.findAllByType(type);
    }

    public WorkflowRequest cancel(final WorkflowRequest request, final SessionUserId sessionUserId) {

        final var now = LocalDateTime.now();

        request.setCanceled(true);
        request.setCanceledBy(sessionUserId.getId());
        request.setCanceledAt(now);

        request.setUpdatedBy(sessionUserId.getId());
        request.setUpdatedAt(now);

        return repository.save(request);
    }

    public WorkflowRequest updateWorkflowRequestStatus(final WorkflowRequest request, final Long actionUserId){
        final var now = LocalDateTime.now();

        request.setUpdatedAt(now);
        request.setUpdatedBy(actionUserId);

        return repository.save(request);
    }

    public WorkflowRequest find(final Long id) {
        return repository.findById(id).orElseThrow(() -> new WorkflowRequestNotFoundException(id));
    }

}
