package com.source.rworkflow.workflowRule.domain.rule;

import com.google.common.collect.Sets;
import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleApprovalDto;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleDto;
import com.source.rworkflow.workflowRule.exception.ApprovalAssigneeCanNotBeCreatedWhenUrgent;
import com.source.rworkflow.workflowRule.exception.AssigneeCanNotBeEmpty;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateAssignee;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRuleCompositeService {
    private final WorkflowRuleService service;

    @Transactional
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
        checkUrgentApproval(request);
        checkDuplicateAssignee(request);
        checkDuplicateOrder(request.getApprovals());
    }

    private void checkDuplicateOrder(final List<WorkflowRuleApprovalDto.Request> requests) {
        final var orders = requests.stream()
                .map(WorkflowRuleApprovalDto.Request::getOrder)
                .collect(Collectors.toUnmodifiableList());

        if (hasDuplicateElement(orders)) {
            throw new CanNotDuplicateOrder();
        }
    }

    private void checkUrgentApproval(final WorkflowRuleDto.Create.Request request) {
        if (request.isUrgent()) {
            if (request.getApprovals() != null | request.getApprovals().size() > 0) {
                throw new ApprovalAssigneeCanNotBeCreatedWhenUrgent();
            }
        }
    }

    private void checkDuplicateAssignee(final WorkflowRuleDto.Create.Request request) {
        if (request.getApprovals() != null) {
            final var assignees = request.getApprovals().stream()
                    .flatMap(approval -> {
                        if (approval.getAssignees() == null) {
                            throw new AssigneeCanNotBeEmpty();
                        }

                        return approval.getAssignees().stream();
                    })
                    .collect(Collectors.toUnmodifiableList());

            if (hasDuplicateElement(assignees)) {
                throw new CanNotDuplicateAssignee();
            }
        }

        if (request.getExecutions() != null) {
            if (hasDuplicateElement(request.getExecutions())) {
                throw new CanNotDuplicateAssignee();
            }
        }

        if (request.getReviews() != null) {
            if (hasDuplicateElement(request.getReviews())) {
                throw new CanNotDuplicateAssignee();
            }
        }
    }

    private boolean hasDuplicateElement(final List<?> list) {
        final var set = Sets.newHashSet(list);
        return set.size() != list.size();
    }

}
