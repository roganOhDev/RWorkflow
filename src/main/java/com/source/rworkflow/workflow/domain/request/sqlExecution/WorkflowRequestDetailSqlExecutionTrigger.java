package com.source.rworkflow.workflow.domain.request.sqlExecution;

import com.source.rworkflow.common.domain.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailSqlExecutionTrigger {
    private final ScheduleService scheduleService;

    public void afterCreate(final WorkflowRequestDetailSqlExecution sqlExecution) {
        scheduleService.enrollJob(sqlExecution.getExecutionExpiryAt(), "sqlExecution_execution_expiry");
        scheduleService.enrollJob(sqlExecution.getRequestExpiryAt(), "sqlExecution_request_expiry");
    }

}
