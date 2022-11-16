package com.source.rworkflow.workflow.domain.request.sqlExecution;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailSqlExecutionTriggerService {
    private final WorkflowRequestDetailSqlExecutionTrigger trigger;
    private final WorkflowRequestDetailSqlExecutionService service;

    public WorkflowRequestDetailSqlExecution create(final WorkflowRequestDetailSqlExecution sqlExecution) {
        final var created = service.create(sqlExecution);

        trigger.enrollSchedulerJob(sqlExecution);

        return created;
    }
}
