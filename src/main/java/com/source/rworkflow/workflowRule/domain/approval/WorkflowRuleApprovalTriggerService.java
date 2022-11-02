package com.source.rworkflow.workflowRule.domain.approval;

import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRuleApprovalTriggerService {
    private final WorkflowRuleApprovalTrigger trigger;
    private final WorkflowRuleApprovalService service;

    public WorkflowRuleApproval create(final WorkflowRuleApproval approval, final List<AssigneeDto.Request> assignees) {
        final var created = service.create(approval);

        trigger.afterCreate(created.getId(), assignees);

        return created;
    }

    public void delete(final WorkflowRuleApproval approval) {
        trigger.beforeDelete(approval);

        service.delete(approval);
    }
}