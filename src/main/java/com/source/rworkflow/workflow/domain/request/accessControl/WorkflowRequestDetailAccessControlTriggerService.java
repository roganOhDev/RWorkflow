package com.source.rworkflow.workflow.domain.request.accessControl;

import com.source.rworkflow.workflow.dto.AccessControlConnectionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailAccessControlTriggerService {
    private final WorkflowRequestDetailAccessControlTrigger trigger;
    private final WorkflowRequestDetailAccessControlService service;

    public WorkflowRequestDetailAccessControl create(final WorkflowRequestDetailAccessControl accessControl, final List<AccessControlConnectionDto.Request> createRequests){
        final var created = service.create(accessControl);

        trigger.afterCreate(created, createRequests);

        return created;
    }

    public void grant(final WorkflowRequestDetailAccessControl accessControl) {
        trigger.beforeGrant(accessControl.getId());
    }
}
