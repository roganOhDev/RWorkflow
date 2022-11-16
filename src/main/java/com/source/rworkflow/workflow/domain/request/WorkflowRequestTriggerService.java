package com.source.rworkflow.workflow.domain.request;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import com.source.rworkflow.workflow.type.ApprovalStatusType;
import com.source.rworkflow.workflow.type.ExecutionStatusType;
import com.source.rworkflow.workflow.type.ReviewStatusType;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestTriggerService {
    private final WorkflowRequestTrigger trigger;
    private final WorkflowRequestService service;

    public WorkflowRequest create(final WorkflowRequest workflowRequest, final WorkflowRequestDto.Create.Request request, final SessionUserId sessionUserId) {
        final var created = service.create(workflowRequest, sessionUserId);

        trigger.createDetail(created.getId(), request);

        return created;
    }

    public WorkflowRequest approve(final WorkflowRequest workflowRequest, final Long order, final SessionUserId sessionUserId) {
        final var approveFinished = trigger.approveApproval(workflowRequest.getId(), order, sessionUserId);

        if (approveFinished) {
            workflowRequest.setApprovalStatus(ApprovalStatusType.APPROVED);
            workflowRequest.setExecutionStatus(ExecutionStatusType.PENDING);

            if (workflowRequest.getType() == WorkflowRequestType.ACCESS_CONTROL) {
                workflowRequest.setExecutionStatus(ExecutionStatusType.SUCCEEDED);
                workflowRequest.setReviewStatus(ReviewStatusType.PENDING);

                trigger.grantAccessControl(workflowRequest.getId());
            }

        } else {
            workflowRequest.setApprovalStatus(ApprovalStatusType.IN_PROGRESS);
        }

        return service.updateWorkflowRequestStatus(workflowRequest, sessionUserId.getId());
    }

    public WorkflowRequest disApprove(final WorkflowRequest workflowRequest, final Long order, final SessionUserId sessionUserId) {
        trigger.disApproveApproval(workflowRequest.getId(), order, sessionUserId);

        workflowRequest.setApprovalStatus(ApprovalStatusType.REJECTED);

        return service.updateWorkflowRequestStatus(workflowRequest, sessionUserId.getId());
    }

    public void execute(final WorkflowRequest workflowRequest, final SessionUserId sessionUserId) {
        trigger.assigneeExecute(workflowRequest.getId(), sessionUserId);

        workflowRequest.setExecutionStatus(ExecutionStatusType.IN_PROGRESS);

        service.updateWorkflowRequestStatus(workflowRequest, sessionUserId.getId());
    }


    public void executeSuccess(final WorkflowRequest request) {
        trigger.executionAssigneeSuccess(request.getId(), request.getUpdatedBy());

        request.setExecutionStatus(ExecutionStatusType.SUCCEEDED);
        request.setReviewStatus(ReviewStatusType.PENDING);

        trigger.setReviewAssigneesPending(request.getId(), request.getUpdatedBy());

        service.updateWorkflowRequestStatus(request, request.getUpdatedBy());
    }

    public void executeFail(final WorkflowRequest request) {
        trigger.executionAssigneeFail(request.getId(), request.getUpdatedBy());

        request.setExecutionStatus(ExecutionStatusType.FAILED);

        service.updateWorkflowRequestStatus(request, request.getUpdatedBy());
    }

    public void review(final WorkflowRequest request, final SessionUserId sessionUserId) {
        final var reviewFinish = trigger.assigneeReview(request.getId(), sessionUserId);

        if (reviewFinish) {
            request.setReviewStatus(ReviewStatusType.CONFIRMED);
        } else {
            request.setReviewStatus(ReviewStatusType.IN_PROGRESS);
        }

        service.updateWorkflowRequestStatus(request, sessionUserId.getId());
    }
}
