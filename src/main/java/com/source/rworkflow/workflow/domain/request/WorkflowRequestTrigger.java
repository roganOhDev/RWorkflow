package com.source.rworkflow.workflow.domain.request;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.domain.approval.WorkflowRequestApprovalCompositeService;
import com.source.rworkflow.workflow.domain.executeAssignee.WorkflowRequestExecutionAssigneeCompositeService;
import com.source.rworkflow.workflow.domain.request.accessControl.WorkflowRequestDetailAccessControl;
import com.source.rworkflow.workflow.domain.request.accessControl.WorkflowRequestDetailAccessControlCompositeService;
import com.source.rworkflow.workflow.domain.request.dataExport.WorkflowRequestDetailDataExport;
import com.source.rworkflow.workflow.domain.request.dataExport.WorkflowRequestDetailDataExportCompositeService;
import com.source.rworkflow.workflow.domain.request.sqlExecution.WorkflowRequestDetailSqlExecution;
import com.source.rworkflow.workflow.domain.request.sqlExecution.WorkflowRequestDetailSqlExecutionCompositeService;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestTrigger {
    private final WorkflowRequestApprovalCompositeService approvalCompositeService;
    private final WorkflowRequestExecutionAssigneeCompositeService executionAssigneeCompositeService;
    private final WorkflowRequestDetailSqlExecutionCompositeService sqlExecutionCompositeService;
    private final WorkflowRequestDetailDataExportCompositeService dataExportCompositeService;
    private final WorkflowRequestDetailAccessControlCompositeService accessControlCompositeService;

    public void afterCreate(final Long requestId, final WorkflowRequestDto.Create.Request request) {
        switch (request.getType()){
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

    public int beforeApprove(final Long requestId, final Long order, final SessionUserId sessionUserId, final boolean approve) {
        return approvalCompositeService.approve(requestId, order, sessionUserId, approve);
    }

    public void beforeExecute(final Long requestId,final SessionUserId sessionUserId) {
        executionAssigneeCompositeService.execute(requestId, sessionUserId);
    }

    private List<WorkflowRequestDetailDataExport> createDataExport(final Long requestId, final WorkflowRequestDto.Create.Request.Detail request) {
        return dataExportCompositeService.createCollection(requestId, request.getRequestExpiryAt(), request.getExecutionExpiryAt(), request.getDataExports());
    }

    private List<WorkflowRequestDetailSqlExecution> createSqlExecution(final Long requestId, final WorkflowRequestDto.Create.Request.Detail request) {
        return sqlExecutionCompositeService.createCollection(requestId, request.getRequestExpiryAt(), request.getExecutionExpiryAt(), request.getSqlExecutions());
    }

    private WorkflowRequestDetailAccessControl createAccessControl(final Long requestId, final WorkflowRequestDto.Create.Request.Detail request) {
        return accessControlCompositeService.create(requestId, request.getRequestExpiryAt(), request.getAccessControl());
    }

}
