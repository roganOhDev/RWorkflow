package com.source.rworkflow.workflow.domain.request.dataExport;

import com.source.rworkflow.common.domain.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailDataExportTrigger {
    private final ScheduleService scheduleService;

    public void afterCreate(final WorkflowRequestDetailDataExport dataExport){
        scheduleService.enrollJob(dataExport.getExecutionExpiryAt(), "dataExport_execution_expiry");
        scheduleService.enrollJob(dataExport.getRequestExpiryAt(), "dataExport_request_expiry");
    }

}
