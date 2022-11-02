package com.source.rworkflow.workflowRule.domain.rule;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRuleCompositeService {
    private final WorkflowRuleService service;

    public WorkflowRule create(final WorkflowRuleDto.Create.Request request, final SessionUserId sessionUserId) {
        final var workflowRule = new WorkflowRule();

        workflowRule.setName(request.getName());
        workflowRule.setRequestType(request.getType());
        workflowRule.setUrgent(request.isUrgent());
        workflowRule.setCreatedBy(sessionUserId.getId());
        workflowRule.setUpdatedBy(sessionUserId.getId());

        return service.create(workflowRule);
    }

}
