package com.source.rworkflow.workflow.domain.request.accessControl;

import com.source.rworkflow.workflow.domain.request.accessControl.connection.WorkflowRequestDetailAccessControlConnectionCompositeService;
import com.source.rworkflow.workflow.dto.AccessControlConnectionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailAccessControlTrigger {
    private final WorkflowRequestDetailAccessControlConnectionCompositeService accessControlConnectionCompositeService;

    public void afterCreate(final Long accessControlId, final List<AccessControlConnectionDto.Request> createRequests){
        accessControlConnectionCompositeService.createCollection(accessControlId, createRequests);
    }

    public void beforeGrant(final Long accessControlId) {
        accessControlConnectionCompositeService.grant(accessControlId);
    }
}
