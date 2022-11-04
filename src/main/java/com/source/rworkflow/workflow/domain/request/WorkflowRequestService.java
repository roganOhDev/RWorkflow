package com.source.rworkflow.workflow.domain.request;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import com.source.rworkflow.workflow.type.ApprovalStatusType;
import com.source.rworkflow.workflow.type.ExecutionStatusType;
import com.source.rworkflow.workflow.type.ReviewStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

}
