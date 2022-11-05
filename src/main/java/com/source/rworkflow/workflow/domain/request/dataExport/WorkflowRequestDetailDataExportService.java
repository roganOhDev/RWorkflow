package com.source.rworkflow.workflow.domain.request.dataExport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailDataExportService {
    private final WorkflowRequestDetailDataExportRepository repository;

    public WorkflowRequestDetailDataExport create(final WorkflowRequestDetailDataExport request) {
        return repository.save(request);
    }
}
