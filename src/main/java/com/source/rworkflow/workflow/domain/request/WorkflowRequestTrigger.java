package com.source.rworkflow.workflow.domain.request;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.misc.user.UserAccessControlService;
import com.source.rworkflow.workflow.domain.approval.WorkflowRequestApprovalCompositeService;
import com.source.rworkflow.workflow.domain.executeAssignee.WorkflowRequestExecutionAssigneeCompositeService;
import com.source.rworkflow.workflow.domain.request.accessControl.WorkflowRequestDetailAccessControl;
import com.source.rworkflow.workflow.domain.request.accessControl.WorkflowRequestDetailAccessControlCompositeService;
import com.source.rworkflow.workflow.domain.request.dataExport.WorkflowRequestDetailDataExport;
import com.source.rworkflow.workflow.domain.request.dataExport.WorkflowRequestDetailDataExportCompositeService;
import com.source.rworkflow.workflow.domain.request.sqlExecution.WorkflowRequestDetailSqlExecution;
import com.source.rworkflow.workflow.domain.request.sqlExecution.WorkflowRequestDetailSqlExecutionCompositeService;
import com.source.rworkflow.workflow.domain.reviewAssignee.WorkflowRequestReviewAssigneeCompositeService;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestTrigger {
    private final WorkflowRequestApprovalCompositeService approvalCompositeService;
    private final WorkflowRequestExecutionAssigneeCompositeService executionAssigneeCompositeService;
    private final WorkflowRequestReviewAssigneeCompositeService reviewAssigneeCompositeService;
    private final WorkflowRequestDetailSqlExecutionCompositeService sqlExecutionCompositeService;
    private final WorkflowRequestDetailDataExportCompositeService dataExportCompositeService;
    private final WorkflowRequestDetailAccessControlCompositeService accessControlCompositeService;
    private final UserAccessControlService userAccessControlService;

    public void createDetail(final Long requestId, final WorkflowRequestDto.Create.Request request) {
        switch (request.getType()) {
            case ACCESS_CONTROL:
                createAccessControl(requestId, request.getDetail());
                break;
            case SQL_EXECUTION:
                createSqlExecution(requestId, request.getDetail());
                break;
            case DATA_EXPORT:
                createDataExport(requestId, request.getDetail());
                break;
        }
    }

    public boolean approveApproval(final Long requestId, final Long order, final SessionUserId sessionUserId) {
        return approvalCompositeService.approve(requestId, order, sessionUserId);
    }

    public void disApproveApproval(final Long requestId, final Long order, final SessionUserId sessionUserId) {
        approvalCompositeService.disApprove(requestId, order, sessionUserId);
    }

    public void assigneeExecute(final Long requestId, final SessionUserId sessionUserId) {
        executionAssigneeCompositeService.execute(requestId, sessionUserId);
    }

    public void executionAssigneeSuccess(final Long requestId, final Long executeUserId) {
        executionAssigneeCompositeService.executeSuccess(requestId, executeUserId);
    }

    public void executionAssigneeFail(final Long requestId, final Long executeUserId) {
        executionAssigneeCompositeService.executeFail(requestId, executeUserId);
    }

    public void grantAccessControl(final Long requestId) {
        accessControlCompositeService.grant(requestId);
        userAccessControlService.update();
    }

    public void setReviewAssigneesPending(final Long requestId, final Long executeUserId) {
        reviewAssigneeCompositeService.makeReviewAssigneesPending(requestId, executeUserId);
    }

    public boolean assigneeReview(final Long requestId, final SessionUserId sessionUserId) {
        return reviewAssigneeCompositeService.review(requestId, sessionUserId);
    }

    private WorkflowRequestDetailDataExport createDataExport(final Long requestId, final WorkflowRequestDto.Create.Request.Detail request) {
        return dataExportCompositeService.create(requestId, request);
    }

    private WorkflowRequestDetailSqlExecution createSqlExecution(final Long requestId, final WorkflowRequestDto.Create.Request.Detail request) {
        return sqlExecutionCompositeService.create(requestId, request);
    }

    private WorkflowRequestDetailAccessControl createAccessControl(final Long requestId, final WorkflowRequestDto.Create.Request.Detail request) {
        return accessControlCompositeService.create(requestId, request);
    }

}
