package com.source.rworkflow.workflow.domain.request.accessControl.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailAccessControlConnectionService {
    private final WorkflowRequestDetailAccessControlConnectionRepository repository;

    public WorkflowRequestDetailAccessControlConnection create(final WorkflowRequestDetailAccessControlConnection request) {
        request.setGranted(false);

        return repository.save(request);
    }
}
