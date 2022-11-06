package com.source.rworkflow.workflowRule.domain.rule;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.common.util.Patch;
import com.source.rworkflow.workflow.domain.request.WorkflowRequest;
import com.source.rworkflow.workflow.exception.AccessControlRequestCanNotBeUrgent;
import com.source.rworkflow.workflow.exception.OrdersMustBeInCrement;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleApprovalDto;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleDto;
import com.source.rworkflow.workflow.exception.ApprovalAssigneeCanNotBeCreatedWhenUrgentException;
import com.source.rworkflow.workflowRule.exception.ApprovalCanNotBeNullException;
import com.source.rworkflow.workflowRule.exception.AssigneeCanNotBeNullException;
import com.source.rworkflow.workflowRule.exception.CanNotDeleteWorkflowRuleException;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateAssigneeException;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateOrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
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
    public WorkflowRule delete(final Long id, final List<WorkflowRequest> usedWorkflowRequests, final SessionUserId sessionUserId) {
        validateDelete(usedWorkflowRequests);

        final var workflowRule = service.find(id);

        workflowRule.setDeleted(true);

        return triggerService.delete(workflowRule, sessionUserId);
    }

    @Transactional
    public WorkflowRule update(final WorkflowRule workflowRule, final WorkflowRuleDto.Update.Request request, final SessionUserId sessionUserId) {
        validateUpdate(request);

        final var entityMapperDto = WorkflowRuleDto.Update.EntityMapperDto.from(request);
        final var updated = Patch.entityByRequest(workflowRule, WorkflowRule.class, entityMapperDto);

        return service.update(updated, sessionUserId);
    }

    @Transactional(readOnly = true)
    public WorkflowRule find(final Long id) {
        return service.find(id);
    }

    private void validateDelete(final List<WorkflowRequest> usingWorkflowRequests) {
        if (usingWorkflowRequests.size() > 0) {
            throw new CanNotDeleteWorkflowRuleException("Rule Is Used In " + usingWorkflowRequests.size() + " Workflows");
        }
    }

    private void validateCreate(final WorkflowRuleDto.Create.Request request) {
        checkUrgentAccessControl(request);
        checkAssigneeNull(request);
        checkUrgentApproval(request);
        checkDuplicateAssignee(request.getApprovals(), request.getExecutionAssignees(), request.getReviewAssignees());
        checkDuplicateOrder(request.getApprovals());
        validateOrder(request);
    }

    private void validateOrder(final WorkflowRuleDto.Create.Request request) {
        if (!request.isUrgent()) {
            final var orders = request.getApprovals().stream()
                    .map(WorkflowRuleApprovalDto.Request::getOrder)
                    .collect(Collectors.toUnmodifiableList());

            if (orders.size() == 0 || (Set.copyOf(orders).containsAll(List.of(1L, 2L, 3L)) || Set.copyOf(orders).containsAll(List.of(1L, 2L)) || orders.equals(List.of(1L)))) {
                return;
            }
            throw new OrdersMustBeInCrement();
        }
    }

    private void checkUrgentAccessControl(final WorkflowRuleDto.Create.Request request) {
        if (request.getType().equals(WorkflowRequestType.ACCESS_CONTROL) && request.isUrgent()) {
            throw new AccessControlRequestCanNotBeUrgent();
        }
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
        if (request.getExecutionAssignees() == null || request.getReviewAssignees() == null) {
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

    private void checkUrgentApproval(final WorkflowRuleDto.Create.Request request) {
        if (request.isUrgent()) {
            if (request.getApprovals() == null || request.getApprovals().size() > 0) {
                throw new ApprovalAssigneeCanNotBeCreatedWhenUrgentException();
            }
        }
    }

    private void checkDuplicateAssignee(final List<WorkflowRuleApprovalDto.Request> workflowRuleApprovals, final List<AssigneeDto.Request> executionAssignees, final List<AssigneeDto.Request> reviewAssignees) {
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

        if (executionAssignees != null) {
            if (ListUtil.hasDuplicateElement(executionAssignees)) {
                throw new CanNotDuplicateAssigneeException();
            }
        }

        if (reviewAssignees != null) {
            if (ListUtil.hasDuplicateElement(reviewAssignees)) {
                throw new CanNotDuplicateAssigneeException();
            }
        }
    }

}
