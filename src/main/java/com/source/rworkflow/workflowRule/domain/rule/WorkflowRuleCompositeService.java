package com.source.rworkflow.workflowRule.domain.rule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Sets;
import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.common.util.Patch;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleApprovalDto;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleDto;
import com.source.rworkflow.workflowRule.exception.ApprovalAssigneeCanNotBeCreatedWhenUrgentException;
import com.source.rworkflow.workflowRule.exception.AssigneeCanNotBeEmptyException;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateAssigneeException;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateOrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRuleCompositeService {
    private final WorkflowRuleTriggerService triggerService;
    private final WorkflowRuleService service;

    @Transactional
    public WorkflowRule create(final WorkflowRuleDto.Create.Request request, final SessionUserId sessionUserId) {
        validate(request, sessionUserId);

        final var workflowRule = new WorkflowRule();

        workflowRule.setName(request.getName());
        workflowRule.setRequestType(request.getType());
        workflowRule.setUrgent(request.isUrgent());
        workflowRule.setDeleted(false);


        return service.create(workflowRule, sessionUserId);
    }

    @Transactional
    public WorkflowRule delete(final WorkflowRule workflowRule, final SessionUserId sessionUserId) {
        workflowRule.setDeleted(true);

        return triggerService.delete(workflowRule, sessionUserId);
    }

    @Transactional
    public WorkflowRule update(final WorkflowRule workflowRule,final WorkflowRuleDto.Update.Request request, final SessionUserId sessionUserId) {
        return Patch.of(workflowRule, WorkflowRule.class, request);
    }

    private void validate(final WorkflowRuleDto.Create.Request request, final SessionUserId sessionUserId) {
        checkUrgentApproval(request);
        checkDuplicateAssignee(request);
        checkDuplicateOrder(request.getApprovals());
    }

    private void checkDuplicateOrder(final List<WorkflowRuleApprovalDto.Request> requests) {
        final var orders = requests.stream()
                .map(WorkflowRuleApprovalDto.Request::getOrder)
                .collect(Collectors.toUnmodifiableList());

        if (hasDuplicateElement(orders)) {
            throw new CanNotDuplicateOrderException();
        }
    }

    private void checkUrgentApproval(final WorkflowRuleDto.Create.Request request) {
        if (request.isUrgent()) {
            if (request.getApprovals() != null | request.getApprovals().size() > 0) {
                throw new ApprovalAssigneeCanNotBeCreatedWhenUrgentException();
            }
        }
    }

    private void checkDuplicateAssignee(final WorkflowRuleDto.Create.Request request) {
        if (request.getApprovals() != null) {
            final var assignees = request.getApprovals().stream()
                    .flatMap(approval -> {
                        if (approval.getAssignees() == null) {
                            throw new AssigneeCanNotBeEmptyException();
                        }

                        return approval.getAssignees().stream();
                    })
                    .collect(Collectors.toUnmodifiableList());

            if (hasDuplicateElement(assignees)) {
                throw new CanNotDuplicateAssigneeException();
            }
        }

        if (request.getExecutions() != null) {
            if (hasDuplicateElement(request.getExecutions())) {
                throw new CanNotDuplicateAssigneeException();
            }
        }

        if (request.getReviews() != null) {
            if (hasDuplicateElement(request.getReviews())) {
                throw new CanNotDuplicateAssigneeException();
            }
        }
    }

    private boolean hasDuplicateElement(final List<?> list) {
        final var set = Sets.newHashSet(list);
        return set.size() != list.size();
    }

}
