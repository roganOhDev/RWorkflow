package com.source.rworkflow.workflow.domain.request.accessControl;

import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflow.dto.AccessControlConnectionDto;
import com.source.rworkflow.workflow.dto.AccessControlDto;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import com.source.rworkflow.workflow.exception.AccessControlRequestNotFoundException;
import com.source.rworkflow.workflow.exception.CanNotDuplicateRequestAccessControlConnectionException;
import com.source.rworkflow.workflow.exception.ExpirationDateIsBeforeNow;
import com.source.rworkflow.workflow.exception.RequestDetailNullException;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailAccessControlCompositeService {
    private final WorkflowRequestDetailAccessControlTriggerService triggerService;
    private final WorkflowRequestDetailAccessControlService service;

    @Transactional
    public WorkflowRequestDetailAccessControl create(final Long requestId, final WorkflowRequestDto.Create.Request.Detail detailRequest) {

        final var createRequest = detailRequest.getAccessControl();

        validate(detailRequest.getAccessControl());

        final var accessControl = new WorkflowRequestDetailAccessControl();

        accessControl.setRequestId(requestId);
        accessControl.setExpirationDate(detailRequest.getRequestExpiryAt());

        return triggerService.create(accessControl, createRequest.getConnections());
    }

    @Transactional
    public void grant(final Long requestId) {
        final var accessControl = service.findByRequestId(requestId);
        if (accessControl == null) {
            throw new AccessControlRequestNotFoundException(requestId);
        }

        triggerService.grant(accessControl);
    }

    @Transactional(readOnly = true)
    public WorkflowRequestDetailAccessControl findByRequestId(final Long requestId){
        return service.findByRequestId(requestId);
    }

    private LocalDateTime getExpirationDate(final LocalDateTime request) {
        final var now = LocalDateTime.now();

        if (request != null) {
            if (request.isBefore(now)) {
                throw new ExpirationDateIsBeforeNow("accessExpirationDate");
            }
            return request;
        }

        return now.plusYears(1);
    }

    private void validate(AccessControlDto.Request createRequest) {
        validateNull(createRequest);
        validateDuplicateConnection(createRequest);
    }

    private void validateDuplicateConnection(AccessControlDto.Request createRequest) {
        final var hasDuplicateElement = ListUtil.hasDuplicateElement(createRequest.getConnections().stream()
                .map(AccessControlConnectionDto.Request::getConnectionId)
                .collect(Collectors.toUnmodifiableList()));

        if (hasDuplicateElement) {
            throw new CanNotDuplicateRequestAccessControlConnectionException();
        }
    }

    private void validateNull(final AccessControlDto.Request createRequest){
        if (createRequest == null) {
            throw new RequestDetailNullException(WorkflowRequestType.ACCESS_CONTROL);
        }
    }

}
