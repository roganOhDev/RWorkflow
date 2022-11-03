package com.source.rworkflow.workflowRule.domain.rule;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.common.util.Patch;
import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleApprovalDto;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleDto;
import com.source.rworkflow.workflowRule.exception.ApprovalAssigneeCanNotBeCreatedWhenUrgentException;
import com.source.rworkflow.workflowRule.exception.ApprovalCanNotBeNullException;
import com.source.rworkflow.workflowRule.exception.AssigneeCanNotBeNullException;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateAssigneeException;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateOrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRuleCompositeService {
    private final WorkflowRuleTriggerService triggerService;
    private final WorkflowRuleService service;

    @Transactional
    public WorkflowRule create(final WorkflowRuleDto.Create.Request request, final SessionUserId sessionUserId) {
        validateCreate(request);

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
    public WorkflowRule update(final WorkflowRule workflowRule, final WorkflowRuleDto.Update.Request request, final boolean hasApproval, final SessionUserId sessionUserId) {
        validateUpdate(request);

        final var updated = Patch.entityByRequest(workflowRule, WorkflowRule.class, request);

        checkUrgentApproval(request.getUrgent(), hasApproval);

        return service.update(updated, sessionUserId);
    }

    private void validateCreate(final WorkflowRuleDto.Create.Request request) {
        checkAssigneeNull(request);
        checkUrgentApproval(request);
        checkDuplicateAssignee(request.getApprovals(), request.getExecutionAssignees(), request.getReviewAssignees());
        checkDuplicateOrder(request.getApprovals());
    }

    private void validateUpdate(final WorkflowRuleDto.Update.Request request) {
        checkDuplicateAssignee(request.getApprovals(), request.getExecutionAssignees(), request.getReviewAssignees());

        if (request.getApprovals() != null) {
            checkDuplicateOrder(request.getApprovals());
        }
    }

    private void checkAssigneeNull(final WorkflowRuleDto.Create.Request request) {
        if (request.getApprovals() == null) {
            throw new ApprovalCanNotBeNullException();
        }
        if (request.getApprovals() == null | request.getExecutionAssignees() == null | request.getReviewAssignees() == null) {
            throw new AssigneeCanNotBeNullException();
        }
    }

    private void checkDuplicateOrder(final List<WorkflowRuleApprovalDto.Request> requests) {
        final var orders = requests.stream()
                .map(WorkflowRuleApprovalDto.Request::getOrder)
                .collect(Collectors.toUnmodifiableList());

        if (ListUtil.hasDuplicateElement(orders)) {
            throw new CanNotDuplicateOrderException();
        }
    }

    private void checkUrgentApproval(final boolean isUrgent, final boolean hasApproval) {
        if (isUrgent != hasApproval) {
            throw new ApprovalAssigneeCanNotBeCreatedWhenUrgentException();
        }
    }

    private void checkUrgentApproval(final WorkflowRuleDto.Create.Request request) {
        if (request.isUrgent()) {
            if (request.getApprovals() == null | request.getApprovals().size() > 0) {
                throw new ApprovalAssigneeCanNotBeCreatedWhenUrgentException();
            }
        }
    }

    private void checkDuplicateAssignee(final List<WorkflowRuleApprovalDto.Request> workflowRuleApprovals, final List<AssigneeDto.Request> executions, final List<AssigneeDto.Request> reviews) {
        if (workflowRuleApprovals != null) {
            final var assignees = workflowRuleApprovals.stream()
                    .flatMap(approval -> {
                        if (approval.getAssignees() == null) {
                            throw new AssigneeCanNotBeNullException();
                        }

                        return approval.getAssignees().stream();
                    })
                    .collect(Collectors.toUnmodifiableList());

            if (ListUtil.hasDuplicateElement(assignees)) {
                throw new CanNotDuplicateAssigneeException();
            }
        }

        if (executions != null) {
            if (ListUtil.hasDuplicateElement(executions)) {
                throw new CanNotDuplicateAssigneeException();
            }
        }

        if (reviews != null) {
            if (ListUtil.hasDuplicateElement(reviews)) {
                throw new CanNotDuplicateAssigneeException();
            }
        }
    }

}
