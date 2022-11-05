package com.source.rworkflow.workflow.domain.request;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflow.dto.WorkflowApprovalDto;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import com.source.rworkflow.workflow.exception.ExecutionExpirationDateMustBeAfterRequestExpirationDateException;
import com.source.rworkflow.workflow.exception.ExpirationDateIsBeforeNow;
import com.source.rworkflow.workflow.exception.OrderMustMatchWithOrderOfRuleException;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
import com.source.rworkflow.workflowRule.domain.approval.WorkflowRuleApproval;
import com.source.rworkflow.workflowRule.exception.AssigneeCanNotBeNullException;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateAssigneeException;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateOrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
    public List<WorkflowRequest> findByRuleId(final Long ruleId) {
        return service.findByRuleId(ruleId);
    }

    private void setDefaultExpirationDateAndValidate(final WorkflowRequestDto.Create.Request request) {

        final var detail = request.getDetail();

        if (request.getDetail().getRequestExpiryAt() == null) {
            detail.setDefaultRequestExpiryAt();
        }

        if (request.getType().isNotAutoExecution() && detail.getExecutionExpiryAt() == null) {
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
        checkDuplicateAssignee(request.getApprovals(), request.getExecutionAssignees(), request.getReviewAssignees());
        validateOrder(request.getApprovals(), workflowRuleSuite);
        checkDuplicateOrder(request.getApprovals());
    }

    private void validateOrder(final List<WorkflowApprovalDto.Create.Request> requests, final WorkflowRuleSuite workflowRuleSuite) {
        if (workflowRuleSuite != null) {
            checkOrderMatch(requests, workflowRuleSuite.getApprovals());
        }
        checkDuplicateOrder(requests);
    }

    private void checkOrderMatch(final List<WorkflowApprovalDto.Create.Request> requests, final List<WorkflowRuleApproval> ruleApprovals) {
        final var requestOrders = requests.stream()
                .map(WorkflowApprovalDto.Create.Request::getOrder)
                .collect(Collectors.toUnmodifiableList());

        final var ruleOrders = ruleApprovals.stream()
                .map(WorkflowRuleApproval::getOrder)
                .collect(Collectors.toUnmodifiableList());

        if (!requestOrders.equals(ruleOrders)) {
            throw new OrderMustMatchWithOrderOfRuleException();
        }
    }

    private void checkDuplicateOrder(final List<WorkflowApprovalDto.Create.Request> requests) {
        final var orders = requests.stream()
                .map(WorkflowApprovalDto.Create.Request::getOrder)
                .collect(Collectors.toUnmodifiableList());

        if (ListUtil.hasDuplicateElement(orders)) {
            throw new CanNotDuplicateOrderException();
        }
    }

    private void checkDuplicateAssignee(final List<WorkflowApprovalDto.Create.Request> workflowRuleApprovals, final List<Long> executionAssignees, final List<Long> reviewAssigneess) {
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
