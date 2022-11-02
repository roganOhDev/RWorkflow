package com.source.rworkflow.workflowRule.domain.rule;

import com.google.common.collect.Sets;
import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleDto;
import com.source.rworkflow.workflowRule.exception.ApprovalAssigneeCanNotBeCreatedWhenUrgent;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateAssignee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRuleCompositeService {
    private final WorkflowRuleService service;

    public WorkflowRule create(final WorkflowRuleDto.Create.Request request, final SessionUserId sessionUserId) {
        validate(request, sessionUserId);

        final var workflowRule = new WorkflowRule();

        workflowRule.setName(request.getName());
        workflowRule.setRequestType(request.getType());
        workflowRule.setUrgent(request.isUrgent());
        workflowRule.setCreatedBy(sessionUserId.getId());
        workflowRule.setUpdatedBy(sessionUserId.getId());

        return service.create(workflowRule);
    }

    private void validate(final WorkflowRuleDto.Create.Request request, final SessionUserId sessionUserId) {
        if (request.isUrgent()) {
            if (request.getApprovals() != null | request.getApprovals().size() > 0) {
                throw new ApprovalAssigneeCanNotBeCreatedWhenUrgent();
            }
        }

        checkDuplicateAssignee(request);
    }

    private void checkDuplicateAssignee(final WorkflowRuleDto.Create.Request request) {
        if (request.getApprovals() != null) {
            final var assignees = request.getApprovals().stream()
                    .flatMap(approval -> approval.getAssignees().stream()
                            .map(AssigneeDto.Request::getValue)
                            .collect(Collectors.toUnmodifiableList()).stream())
                    .collect(Collectors.toUnmodifiableList());

            checkDuplicateElement(assignees);
        }

        if (request.getExecutions() != null) {
            final var assignees = request.getExecutions().stream()
                    .map(AssigneeDto.Request::getValue)
                    .collect(Collectors.toUnmodifiableList());

            checkDuplicateElement(assignees);
        }

        if (request.getReviews() != null) {
            final var assignees = request.getReviews().stream()
                    .map(AssigneeDto.Request::getValue)
                    .collect(Collectors.toUnmodifiableList());

            checkDuplicateElement(assignees);
        }
    }

    private void checkDuplicateElement(final List<?> list) {
        final var set = Sets.newHashSet(list);

        if (set.size() != list.size()) {
            throw new CanNotDuplicateAssignee();
        }
    }

}
