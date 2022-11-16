package com.source.rworkflow.workflow.domain.request.dataExport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailDataExportTriggerService {
    private final WorkflowRequestDetailDataExportTrigger trigger;
    private final WorkflowRequestDetailDataExportService service;

    public WorkflowRequestDetailDataExport create(final WorkflowRequestDetailDataExport dataExport){
        final var created = service.create(dataExport);

        trigger.enrollSchedulerJob(dataExport);

        return created;
    }
}
