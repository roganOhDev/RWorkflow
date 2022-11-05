package com.source.rworkflow.workflow.domain.request.accessControl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailAccessControlService {
    private final WorkflowRequestDetailAccessControlRepository repository;

    public WorkflowRequestDetailAccessControl create(final WorkflowRequestDetailAccessControl request) {

        return repository.save(request);
    }

    public WorkflowRequestDetailAccessControl findByRequestId(final Long requestId) {
        return repository.findByRequestId(requestId);
    }
}
