package com.source.rworkflow.workflow.controller;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.misc.user.AssigneeDto;
import com.source.rworkflow.misc.user.UserService;
import com.source.rworkflow.workflow.domain.Assignee;
import com.source.rworkflow.workflow.domain.approval.WorkflowRequestApprovalCompositeService;
import com.source.rworkflow.workflow.domain.approval.assignee.WorkflowRequestApprovalAssignee;
import com.source.rworkflow.workflow.domain.approval.assignee.WorkflowRequestApprovalAssigneeCompositeService;
import com.source.rworkflow.workflow.domain.executeAssignee.WorkflowRequestExecutionAssigneeCompositeService;
import com.source.rworkflow.workflow.domain.request.WorkflowRequestCompositeService;
import com.source.rworkflow.workflow.domain.request.accessControl.WorkflowRequestDetailAccessControlCompositeService;
import com.source.rworkflow.workflow.domain.request.accessControl.connection.WorkflowRequestDetailAccessControlConnectionCompositeService;
import com.source.rworkflow.workflow.domain.request.dataExport.WorkflowRequestDetailDataExportCompositeService;
import com.source.rworkflow.workflow.domain.request.sqlExecution.WorkflowRequestDetailSqlExecutionCompositeService;
import com.source.rworkflow.workflow.domain.reviewAssignee.WorkflowRequestReviewAssigneeCompositeService;
import com.source.rworkflow.workflow.dto.WorkflowApprovalDto;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import com.source.rworkflow.workflow.exception.OrderMustMatchWithOrderOfRuleException;
import com.source.rworkflow.workflow.exception.TypeNotMatchException;
import com.source.rworkflow.workflow.exception.UrgentValueNotMatchException;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuiteFactory;
import com.source.rworkflow.workflowRule.domain.approval.WorkflowRuleApproval;
import com.source.rworkflow.workflowRule.domain.rule.WorkflowRule;
import com.source.rworkflow.workflowRule.domain.rule.WorkflowRuleCompositeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowTransferService {
    private final WorkflowRequestCompositeService compositeService;
    private final WorkflowRequestApprovalCompositeService workflowRequestApprovalCompositeService;
    private final WorkflowRequestApprovalAssigneeCompositeService workflowRequestApprovalAssigneeCompositeService;
    private final WorkflowRequestExecutionAssigneeCompositeService workflowRequestExecutionAssigneeCompositeService;
    private final WorkflowRequestReviewAssigneeCompositeService workflowRequestReviewAssigneeCompositeService;

    private final WorkflowRequestDetailAccessControlCompositeService workflowRequestDetailAccessControlCompositeService;
    private final WorkflowRequestDetailAccessControlConnectionCompositeService workflowRequestDetailAccessControlConnectionCompositeService;
    private final WorkflowRequestDetailSqlExecutionCompositeService workflowRequestDetailSqlExecutionCompositeService;
    private final WorkflowRequestDetailDataExportCompositeService workflowRequestDetailDataExportCompositeService;

    private final UserService userService;

    private final WorkflowRuleCompositeService workflowRuleCompositeService;
    private final WorkflowRuleSuiteFactory workflowRuleSuiteFactory;

    public WorkflowRequestDto.Response create(final WorkflowRequestDto.Create.Request createRequest, final SessionUserId sessionUserId) {
        WorkflowRuleSuite workflowRuleSuite = null;
        final var approvalAssignees = new HashMap<Long, List<AssigneeDto>>();

        if (createRequest.getRuleId() != null) {
            final var workflowRule = workflowRuleCompositeService.find(createRequest.getRuleId());
            workflowRuleSuite = workflowRuleSuiteFactory.of(workflowRule);

            validateWithRule(workflowRule, workflowRuleSuite, createRequest);
        }

        final var workflowRequest = compositeService.create(createRequest, sessionUserId, workflowRuleSuite);

        final var approvals = workflowRequestApprovalCompositeService.createCollection(workflowRequest.getId(), createRequest.getApprovals(), workflowRuleSuite, sessionUserId);
        approvals.forEach(approval -> approvalAssignees.putAll(approvalAssigneeMap(approval.getId())));

        final var executionAssignees = changeToUserDtos(workflowRequestExecutionAssigneeCompositeService.createCollection(workflowRequest.getId(), createRequest.getExecutionAssignees(), workflowRuleSuite));
        final var reviewAssignees = changeToUserDtos(workflowRequestReviewAssigneeCompositeService.createCollection(workflowRequest.getId(), createRequest.getReviewAssignees(), workflowRuleSuite));


        return new WorkflowRequestDto.Response(workflowRequest, approvals, approvalAssignees, executionAssignees, reviewAssignees);
    }

    public WorkflowRequestDto.DetailResponse find(final Long id) {
        final var approvalAssignees = new HashMap<Long, List<AssigneeDto>>();

        final var workflowRequest = compositeService.find(id);
        final var approvals = workflowRequestApprovalCompositeService.findAllByRequestId(workflowRequest.getId());
        approvals.forEach(approval -> approvalAssignees.putAll(approvalAssigneeMap(approval.getId())));
        final var executionAssignees = changeToUserDtos(workflowRequestExecutionAssigneeCompositeService.findByRequestId(workflowRequest.getId()));
        final var reviewAssignees = changeToUserDtos(workflowRequestReviewAssigneeCompositeService.findByRequestId(workflowRequest.getId()));

        final var detailAccessControl = workflowRequestDetailAccessControlCompositeService.findByRequestId(workflowRequest.getId());
        final var detailAccessControlConnections = detailAccessControl == null
                ? null
                : workflowRequestDetailAccessControlConnectionCompositeService.findAllByDetailAccessControlId(detailAccessControl.getId());
        final var detailSqlExecutions = workflowRequestDetailSqlExecutionCompositeService.findAllByRequestId(workflowRequest.getId());
        final var detailDataExecutions = workflowRequestDetailDataExportCompositeService.findAllByRequestId(workflowRequest.getId());

        return new WorkflowRequestDto.DetailResponse(workflowRequest, approvals, approvalAssignees, executionAssignees, reviewAssignees,
                detailAccessControl, detailAccessControlConnections, detailSqlExecutions, detailDataExecutions);
    }

    public List<WorkflowRequestDto.Response> list(final WorkflowRequestType type) {
        final var workflowRequests = compositeService.findAllByType(type);

        final var approvals = workflowRequestApprovalCompositeService.findAll();

        final var mappedApprovalAssignees = new HashMap<Long, Map<Long, ArrayList<AssigneeDto>>>();
        workflowRequestApprovalAssigneeCompositeService.findAll()
                .forEach(assignee -> pushInTriple(mappedApprovalAssignees, assignee));

        final var executionAssignees = workflowRequestExecutionAssigneeCompositeService.findAll().stream()
                .collect(Collectors.groupingBy(Assignee::getRequestId));

        final var reviewAssignees = workflowRequestReviewAssigneeCompositeService.findAll().stream()
                .collect(Collectors.groupingBy(Assignee::getRequestId));


        return workflowRequests.stream()
                .map(workflowRequest -> new WorkflowRequestDto.Response(workflowRequest, approvals.get(workflowRequest.getId()),
                        mappedApprovalAssignees.get(workflowRequest.getId()), changeToUserDtos(executionAssignees.get(workflowRequest.getId())),
                        changeToUserDtos(reviewAssignees.get(workflowRequest.getId()))))

                .collect(Collectors.toUnmodifiableList());
    }

    public WorkflowRequestDto.Cancel.Response cancel(final Long id, final SessionUserId sessionUserId) {
        final var canceled = compositeService.cancel(id, sessionUserId);

        return new WorkflowRequestDto.Cancel.Response(canceled.getType(), canceled.getId(), canceled.getTitle(), canceled.isCanceled());
    }

    public WorkflowRequestDto.Approve.Response approve(final Long id, final Long order, final SessionUserId sessionUserId, final boolean approve) {
        final var updated = compositeService.approve(id, order, sessionUserId, approve);

        final var approvalAssignees = new HashMap<Long, List<AssigneeDto>>();

        final var approvals = workflowRequestApprovalCompositeService.findAllByRequestId(id);
        approvals.forEach(approval -> approvalAssignees.putAll(approvalAssigneeMap(approval.getId())));

        return WorkflowRequestDto.Approve.Response.from(updated, approvals, approvalAssignees);
    }

    private void pushInTriple(final Map<Long, Map<Long, ArrayList<AssigneeDto>>> mappedApprovalAssignees, WorkflowRequestApprovalAssignee assignee) {
        final var first = assignee.getRequestId();
        final var second = assignee.getRequestApprovalId();
        final var third = changeToUserDto(assignee);

        if (mappedApprovalAssignees.containsKey(first)) {
            if (mappedApprovalAssignees.get(first).containsKey(second)) {
                mappedApprovalAssignees.get(first).get(second).add(third);

            } else {
                mappedApprovalAssignees.get(first).put(second, new ArrayList<>(List.of(third)));
            }
        } else {
            mappedApprovalAssignees.put(first, Map.of(second, new ArrayList<>(List.of(third))));
        }
    }

    private Map<Long, List<AssigneeDto>> approvalAssigneeMap(final Long approvalId) {
        final var assigneeUser = getUserDtosByApprovalId(approvalId);
        return Map.of(approvalId, assigneeUser);
    }

    private List<AssigneeDto> getUserDtosByApprovalId(final Long approvalId) {
        return changeToUserDtos(workflowRequestApprovalAssigneeCompositeService.findAllByApprovalId(approvalId));
    }

    private List<AssigneeDto> changeToUserDtos(final List<? extends Assignee> assignees) {
            return assignees.stream()
                    .map(this::changeToUserDto)
                    .collect(Collectors.toUnmodifiableList());
    }

    private AssigneeDto changeToUserDto(final Assignee assignee) {
        return AssigneeDto.of(userService.find(assignee.getAssigneeId()));
    }

    private void validateWithRule(final WorkflowRule workflowRule, final WorkflowRuleSuite workflowRuleSuite, final WorkflowRequestDto.Create.Request createRequest) {
        validateType(workflowRule, createRequest);
        validateUrgent(workflowRule, createRequest);
        validateOrder(createRequest.getApprovals(), workflowRuleSuite.getApprovals());
    }

    private void validateType(final WorkflowRule workflowRule, final WorkflowRequestDto.Create.Request createRequest) {
        if (!workflowRule.getRequestType().equals(createRequest.getType())) {
            throw new TypeNotMatchException();
        }
    }

    private void validateUrgent(final WorkflowRule workflowRule, final WorkflowRequestDto.Create.Request createRequest) {
        if (workflowRule.isUrgent() != createRequest.isUrgent()) {
            throw new UrgentValueNotMatchException();
        }
    }

    private void validateOrder(final List<WorkflowApprovalDto.Create.Request> requests, final List<WorkflowRuleApproval> ruleApprovals) {
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
}
