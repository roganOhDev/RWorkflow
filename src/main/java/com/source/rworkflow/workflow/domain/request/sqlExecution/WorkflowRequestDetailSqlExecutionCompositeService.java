package com.source.rworkflow.workflow.domain.request.sqlExecution;

import com.source.rworkflow.workflow.dto.SqlExecutionDto;
import com.source.rworkflow.workflow.exception.RequestDetailNullException;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailSqlExecutionCompositeService {
    private final WorkflowRequestDetailSqlExecutionService service;

    @Transactional
    public List<WorkflowRequestDetailSqlExecution> createCollection(final Long requestId, final LocalDateTime requestExpiryAt,
                                                                  final LocalDateTime executionExpiryAt, final List<SqlExecutionDto.Request> createRequests) {

        if (createRequests == null) {
        throw new RequestDetailNullException(WorkflowRequestType.SQl_EXECUTION);
    }

        return createRequests.stream()
                .map(createRequest -> create(requestId, requestExpiryAt, executionExpiryAt, createRequest))
            .collect(Collectors.toUnmodifiableList());
}

    private WorkflowRequestDetailSqlExecution create(final Long requestId, final LocalDateTime requestExpiryAt,
                                                     final LocalDateTime executionExpiryAt, final SqlExecutionDto.Request createRequest) {
        final var sqlExecution = new WorkflowRequestDetailSqlExecution();

        sqlExecution.setRequestId(requestId);
        sqlExecution.setDatabase(createRequest.getDatabase());
        sqlExecution.setConnectionId(createRequest.getConnectionId());
        sqlExecution.setRequestExpiryAt(requestExpiryAt);
        sqlExecution.setExecutionExpiryAt(executionExpiryAt);
        sqlExecution.setContentValue(createRequest.getContentValue());
        sqlExecution.setContentType(createRequest.getContentType());

        return service.create(sqlExecution);
    }
}
