package com.source.rworkflow.workflow.domain.request.dataExport;

import com.source.rworkflow.workflow.dto.DataExportDto;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
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
    public WorkflowRequestDetailDataExport create(final Long requestId, final WorkflowRequestDto.Create.Request.Detail detailRequest) {

        final var createRequest = detailRequest.getDataExport();
        if (createRequest == null) {
            throw new RequestDetailNullException(WorkflowRequestType.DATA_EXPORT);
        }

        final var dataExport = new WorkflowRequestDetailDataExport();

        dataExport.setRequestId(requestId);
        dataExport.setDatabase(createRequest.getDatabase());
        dataExport.setConnectionId(createRequest.getConnectionId());
        dataExport.setRequestExpiryAt(detailRequest.getRequestExpiryAt());
        dataExport.setExecutionExpiryAt(detailRequest.getExecutionExpiryAt());
        dataExport.setContentValue(createRequest.getContentValue());
        dataExport.setContentType(createRequest.getContentType());

        return service.create(dataExport);
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequestDetailDataExport> findAllByRequestId(final Long requestId) {
        return service.findAllByRequestId(requestId);
    }


}
