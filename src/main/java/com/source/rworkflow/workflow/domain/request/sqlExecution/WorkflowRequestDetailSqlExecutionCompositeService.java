package com.source.rworkflow.workflow.domain.request.sqlExecution;

import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import com.source.rworkflow.workflow.exception.RequestDetailNullException;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailSqlExecutionCompositeService {
    private final WorkflowRequestDetailSqlExecutionService service;

    @Transactional
    public WorkflowRequestDetailSqlExecution create(final Long requestId, final WorkflowRequestDto.Create.Request.Detail detailRequest) {

        final var createRequest = detailRequest.getSqlExecution();
        if (createRequest == null) {
            throw new RequestDetailNullException(WorkflowRequestType.SQL_EXECUTION);
        }

        final var sqlExecution = new WorkflowRequestDetailSqlExecution();

        sqlExecution.setRequestId(requestId);
        sqlExecution.setDatabase(createRequest.getDatabase());
        sqlExecution.setConnectionId(createRequest.getConnectionId());
        sqlExecution.setRequestExpiryAt(detailRequest.getRequestExpiryAt());
        sqlExecution.setExecutionExpiryAt(detailRequest.getExecutionExpiryAt());
        sqlExecution.setContentValue(createRequest.getContentValue());
        sqlExecution.setContentType(createRequest.getContentType());

        return service.create(sqlExecution);
    }

    @Transactional(readOnly = true)
    public WorkflowRequestDetailSqlExecution findByRequestId(final Long requestId) {
        return service.findByRequestId(requestId);
    }

    @Transactional
    public void executionExpire(final Long requestId) {
        final var sqlExecution = service.findByRequestId(requestId);

        if (sqlExecution == null) {
            return;
        }

        sqlExecution.set
        sqlExecution.setExecutionExpiryAt(); setExpirationDate(LocalDateTime.now());

        service.expire(sqlExecution);
    }
