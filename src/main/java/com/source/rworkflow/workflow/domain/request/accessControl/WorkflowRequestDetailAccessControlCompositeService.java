package com.source.rworkflow.workflow.domain.request.accessControl;

import com.source.rworkflow.workflow.dto.AccessControlDto;
import com.source.rworkflow.workflow.exception.RequestDetailNullException;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailAccessControlCompositeService {
    private final WorkflowRequestDetailAccessControlTriggerService triggerService;
    private final WorkflowRequestDetailAccessControlService service;

    @Transactional
    public WorkflowRequestDetailAccessControl create(final Long requestId, final LocalDateTime requestExpiryAt,
                                                           final AccessControlDto.Request createRequest) {

        if (createRequest == null) {
            throw new RequestDetailNullException(WorkflowRequestType.ACCESS_CONTROL);
        }

        final var accessControl = new WorkflowRequestDetailAccessControl();

        accessControl.setRequestId(requestId);
        accessControl.setRequestExpiryAt(requestExpiryAt);

        return triggerService.create(accessControl, createRequest.getConnections());
    }

    @Transactional(readOnly = true)
    public WorkflowRequestDetailAccessControl findByRequestId(final Long requestId){
        return service.findByRequestId(requestId);
    }
}
