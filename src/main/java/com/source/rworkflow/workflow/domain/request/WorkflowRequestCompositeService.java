package com.source.rworkflow.workflow.domain.request;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflow.dto.AssigneeDto;
import com.source.rworkflow.workflow.dto.WorkflowApprovalDto;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import com.source.rworkflow.workflow.exception.AccessControlRequestCanNotBeUrgent;
import com.source.rworkflow.workflow.exception.CanNotApproveByUrgentException;
import com.source.rworkflow.workflow.exception.ExecutionExpirationDateMustBeAfterRequestExpirationDateException;
import com.source.rworkflow.workflow.exception.ExpirationDateIsBeforeNow;
import com.source.rworkflow.workflow.exception.OrdersMustBeInCrement;
import com.source.rworkflow.workflow.exception.WorkflowIsCanceledException;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
import com.source.rworkflow.workflow.exception.ApprovalAssigneeCanNotBeCreatedWhenUrgentException;
import com.source.rworkflow.workflowRule.exception.AssigneeCanNotBeNullException;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateAssigneeException;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateOrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRequestCompositeService {
    private final WorkflowRequestTriggerService triggerService;
    private final WorkflowRequestService service;

    @Transactional
    public WorkflowRequest create(final WorkflowRequestDto.Create.Request request, final SessionUserId sessionUserId, final WorkflowRuleSuite workflowRuleSuite) {
        setDefaultExpirationDateAndValidate(request);
        validateCreate(request, workflowRuleSuite);

        final var workflowRequest = new WorkflowRequest();

        workflowRequest.setTitle(request.getTitle());
        workflowRequest.setType(request.getType());
        workflowRequest.setRuleId(request.getRuleId());
        workflowRequest.setUrgent(request.isUrgent());
        workflowRequest.setComment(request.getComment());

        return triggerService.create(workflowRequest, request, sessionUserId);
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequest> findAllByType(WorkflowRequestType type) {
        return service.findAllByType(type);
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequest> findByRuleId(final Long ruleId) {
        return service.findByRuleId(ruleId);
    }

    @Transactional(readOnly = true)
    public WorkflowRequest find(final Long id) {
        return service.find(id);
    }

    @Transactional
    public WorkflowRequest cancel(final Long id, final SessionUserId sessionUserId) {
        final var workflowRequest = service.find(id);

        validateAction(workflowRequest);

        return service.cancel(workflowRequest, sessionUserId);
    }

    @Transactional
    public WorkflowRequest approve(final Long id, final Long order, final SessionUserId sessionUserId, final boolean approve) {
        final var workflowRequest = service.find(id);

        validateAction(workflowRequest);
        validateUrgent(workflowRequest.isUrgent());

        if (approve) {
            return triggerService.approveOk(workflowRequest, order, sessionUserId);
        } else {
            return triggerService.approveReject(workflowRequest, order, sessionUserId);
        }

    }

    private void validateAction(final WorkflowRequest workflowRequest){
        validateCancel(workflowRequest);
    }

    private void validateCancel(final WorkflowRequest workflowRequest) {
        if (workflowRequest.isCanceled()) {
            throw new WorkflowIsCanceledException(workflowRequest.getId());
        }
    }

    private void validateUrgent(final boolean urgent) {
        if (urgent) {
            throw new CanNotApproveByUrgentException();
        }
    }

    private void setDefaultExpirationDateAndValidate(final WorkflowRequestDto.Create.Request request) {

        final var detail = request.getDetail();

        if (detail.getRequestExpiryAt() == null) {
            detail.setDefaultRequestExpiryAt();
        }

        if (detail.getExecutionExpiryAt() == null) {
            detail.setDefaultExecutionExpiryAt();
        }

        validateExpirationDate(request.getType().isNotAutoExecution(), detail);
    }

    private void validateExpirationDate(final boolean isNotAutoExecution, final WorkflowRequestDto.Create.Request.Detail detail) {

        final var now = LocalDateTime.now();

        if (isNotAutoExecution && detail.getRequestExpiryAt().isBefore(detail.getExecutionExpiryAt())) {
            throw new ExecutionExpirationDateMustBeAfterRequestExpirationDateException();
        }

        if (detail.getRequestExpiryAt().isBefore(now)) {
            throw new ExpirationDateIsBeforeNow("RequestExpiryAt");
        }

        if (detail.getExecutionExpiryAt().isBefore(now)) {
            throw new ExpirationDateIsBeforeNow("ExecutionExpiryAt");
        }
    }

    private void validateCreate(final WorkflowRequestDto.Create.Request request, final WorkflowRuleSuite workflowRuleSuite) {
        checkUrgentAccessControl(request);
        checkUrgentApproval(request);
        checkDuplicateAssignee(request.getApprovals(), request.getExecutionAssignees(), request.getReviewAssignees());
        validateOrder(request.getApprovals(), workflowRuleSuite);
        checkDuplicateOrder(request.getApprovals());
    }

    private void checkUrgentAccessControl(final WorkflowRequestDto.Create.Request request) {
        if (request.getType().equals(WorkflowRequestType.ACCESS_CONTROL) && request.isUrgent()) {
            throw new AccessControlRequestCanNotBeUrgent();
        }
    }

    private void checkUrgentApproval(final WorkflowRequestDto.Create.Request request) {
        if (request.isUrgent()) {
            if (request.getApprovals() == null || request.getApprovals().size() > 0) {
                throw new ApprovalAssigneeCanNotBeCreatedWhenUrgentException();
            }
        }
    }

    private void validateOrder(final List<WorkflowApprovalDto.Create.Request> requests, final WorkflowRuleSuite workflowRuleSuite) {
        checkDuplicateOrder(requests);
        validateIncreasment(requests);
    }

    private void validateIncreasment(final List<WorkflowApprovalDto.Create.Request> requests) {
        final var orders = requests.stream()
                .map(WorkflowApprovalDto.Create.Request::getOrder)
                .collect(Collectors.toUnmodifiableList());

        if (orders.size() == 0 || (Set.copyOf(orders).containsAll(List.of(1L, 2L, 3L)) || Set.copyOf(orders).containsAll(List.of(1L, 2L)) || orders.equals(List.of(1L)))) {
            return;
        }
        throw new OrdersMustBeInCrement();
    }

    private void checkDuplicateOrder(final List<WorkflowApprovalDto.Create.Request> requests) {
        final var orders = requests.stream()
                .map(WorkflowApprovalDto.Create.Request::getOrder)
                .collect(Collectors.toUnmodifiableList());

        if (ListUtil.hasDuplicateElement(orders)) {
            throw new CanNotDuplicateOrderException();
        }
    }

    private void checkDuplicateAssignee(final List<WorkflowApprovalDto.Create.Request> workflowRuleApprovals, final List<AssigneeDto.Request> executionAssignees, final List<AssigneeDto.Request> reviewAssigneess) {
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

        if (reviewAssigneess != null) {
            if (ListUtil.hasDuplicateElement(reviewAssigneess)) {
                throw new CanNotDuplicateAssigneeException();
            }
        }
    }
}
