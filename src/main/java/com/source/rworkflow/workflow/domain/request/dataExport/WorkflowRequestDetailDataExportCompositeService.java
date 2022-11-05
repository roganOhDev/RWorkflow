package com.source.rworkflow.workflow.domain.request.dataExport;

import com.source.rworkflow.workflow.dto.DataExportDto;
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
public class WorkflowRequestDetailDataExportCompositeService {
    private final WorkflowRequestDetailDataExportService service;

    @Transactional
    public List<WorkflowRequestDetailDataExport> createCollection(final Long requestId, final LocalDateTime requestExpiryAt,
                                                                  final LocalDateTime executionExpiryAt, final List<DataExportDto.Request> createRequests) {

        if (createRequests == null) {
            throw new RequestDetailNullException(WorkflowRequestType.DATA_EXPORT);
        }

        return createRequests.stream()
                .map(createRequest -> create(requestId, requestExpiryAt, executionExpiryAt, createRequest))
                .collect(Collectors.toUnmodifiableList());
    }

    private WorkflowRequestDetailDataExport create(final Long requestId, final LocalDateTime requestExpiryAt,
                                                   final LocalDateTime executionExpiryAt, final DataExportDto.Request createRequest) {
        final var dataExport = new WorkflowRequestDetailDataExport();

        dataExport.setRequestId(requestId);
        dataExport.setDatabase(createRequest.getDatabase());
        dataExport.setConnectionId(createRequest.getConnectionId());
        dataExport.setRequestExpiryAt(requestExpiryAt);
        dataExport.setExecutionExpiryAt(executionExpiryAt);
        dataExport.setContentValue(createRequest.getContentValue());
        dataExport.setContentType(createRequest.getContentType());

        return service.create(dataExport);
    }
}
