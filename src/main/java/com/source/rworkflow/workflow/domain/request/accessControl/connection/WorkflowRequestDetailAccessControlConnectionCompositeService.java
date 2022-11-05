package com.source.rworkflow.workflow.domain.request.accessControl.connection;

import com.source.rworkflow.workflow.dto.AccessControlConnectionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailAccessControlConnectionCompositeService {
    private final WorkflowRequestDetailAccessControlConnectionService service;

    @Transactional
    public List<WorkflowRequestDetailAccessControlConnection> createCollection(final Long accessControlId, final List<AccessControlConnectionDto.Request> createRequests) {
        return createRequests.stream()
                .map(createRequest -> create(accessControlId, createRequest))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequestDetailAccessControlConnection> findAllByDetailAccessControlId(final Long accessControlId) {
        return service.findAllByDetailAccessControlId(accessControlId);
    }

    private WorkflowRequestDetailAccessControlConnection create(final Long accessControlId, final AccessControlConnectionDto.Request request) {
        final var connection = new WorkflowRequestDetailAccessControlConnection();

        connection.setConnectionId(request.getConnectionId());
        connection.setPrivilegeId(request.getPrivilegeId());
        connection.setConnectionId(accessControlId);

        return service.create(connection);
    }
}
