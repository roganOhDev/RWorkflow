package com.source.rworkflow.workflow.domain.request;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestTriggerService {
    private final WorkflowRequestTrigger trigger;
    private final WorkflowRequestService service;

    public WorkflowRequest create(final WorkflowRequest workflowRequest, final WorkflowRequestDto.Create.Request request, final SessionUserId sessionUserId) {
        final var created = service.create(workflowRequest, sessionUserId);

        trigger.afterCreate(created.getId(), request);

        return created;
    }

    public WorkflowRequest cancel(final WorkflowRequest workflowRequest, final SessionUserId sessionUserId) {
        trigger.beforeCancel(workflowRequest);

        return service.cancel(workflowRequest, sessionUserId);

    }

}
