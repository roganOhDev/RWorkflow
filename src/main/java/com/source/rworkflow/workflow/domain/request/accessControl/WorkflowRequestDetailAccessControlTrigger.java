package com.source.rworkflow.workflow.domain.request.accessControl;

import com.source.rworkflow.common.domain.ScheduleService;
import com.source.rworkflow.workflow.domain.request.accessControl.connection.WorkflowRequestDetailAccessControlConnectionCompositeService;
import com.source.rworkflow.workflow.dto.AccessControlConnectionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailAccessControlTrigger {
    private final WorkflowRequestDetailAccessControlConnectionCompositeService accessControlConnectionCompositeService;
    private final ScheduleService scheduleService;

    public void afterCreate(final WorkflowRequestDetailAccessControl accessControlId, final List<AccessControlConnectionDto.Request> createRequests){
        accessControlConnectionCompositeService.createCollection(accessControlId.getId(), createRequests);
        scheduleService.enrollJob(accessControlId.getExpirationDate(), "accessControl_expiration");
    }

    public void beforeGrant(final Long accessControlId) {
        accessControlConnectionCompositeService.grant(accessControlId);
    }
}
