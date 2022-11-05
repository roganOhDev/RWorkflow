package com.source.rworkflow.workflow.domain.request.accessControl;

import com.source.rworkflow.workflow.dto.AccessControlConnectionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailAccessControlTriggerService {
    private WorkflowRequestDetailAccessControlTrigger trigger;
    private WorkflowRequestDetailAccessControlService service;

    public WorkflowRequestDetailAccessControl create(final WorkflowRequestDetailAccessControl accessControl, final List<AccessControlConnectionDto.Request> createRequests){
        final var created = service.create(accessControl);

        trigger.afterCreate(created.getId(), createRequests);

        return created;
    }
}
