package com.source.rworkflow.workflow.domain.request.accessControl.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailAccessControlConnectionService {
    private final WorkflowRequestDetailAccessControlConnectionRepository repository;

    public WorkflowRequestDetailAccessControlConnection create(final WorkflowRequestDetailAccessControlConnection request) {
        request.setGranted(false);

        return repository.save(request);
    }

    public void grant(final WorkflowRequestDetailAccessControlConnection request) {
        repository.save(request);
    }

    public List<WorkflowRequestDetailAccessControlConnection> findAllByDetailAccessControlId(final Long accessControlId) {
        return repository.findAllByDetailAccessControlId(accessControlId);
    }
}
