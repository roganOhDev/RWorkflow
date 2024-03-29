package com.source.rworkflow.workflow.controller;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.misc.connection.ConnectionService;
import com.source.rworkflow.misc.role.RoleService;
import com.source.rworkflow.misc.user.role.UserRoleService;
import com.source.rworkflow.workflow.domain.request.WorkflowRequest;
import com.source.rworkflow.workflow.dto.AssigneeDto;
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
import com.source.rworkflow.workflowRule.type.AssigneeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final UserRoleService userRoleService;

    private final RoleService roleService;
    private final ConnectionService connectionService;

    private final WorkflowRuleCompositeService workflowRuleCompositeService;
    private final WorkflowRuleSuiteFactory workflowRuleSuiteFactory;

    @Transactional
    public WorkflowRequestDto.Response create(final WorkflowRequestDto.Create.Request createRequest, final SessionUserId sessionUserId) {
        WorkflowRuleSuite workflowRuleSuite = null;
        final var approvalAssignees = new HashMap<Long, List<AssigneeDto.Response>>();

        if (createRequest.getRuleId() != null) {
            final var workflowRule = workflowRuleCompositeService.find(createRequest.getRuleId());
            workflowRuleSuite = workflowRuleSuiteFactory.of(workflowRule);

            validateWithRule(workflowRule, workflowRuleSuite, createRequest);
        }

        validateDetail(createRequest.getDetail());

        final var workflowRequest = compositeService.create(createRequest, sessionUserId);

        final var requestApprovalAssignees = convertApprovalAssigneesToUsers(createRequest.getApprovals());
        final var approvals = workflowRequestApprovalCompositeService.createCollection(workflowRequest.getId(), createRequest.getApprovals(), requestApprovalAssignees, workflowRuleSuite, workflowRequest.isUrgent(), sessionUserId);

        approvals.forEach(approval -> approvalAssignees.putAll(approvalAssigneeMap(approval.getId())));

        final var requestExecutionAssignees = convertRoleToUsers(createRequest.getExecutionAssignees());
        final var executionAssignees = changeToAssigneeDtos(workflowRequestExecutionAssigneeCompositeService.createCollection(workflowRequest, requestExecutionAssignees, workflowRuleSuite));

        final var requestReviewAssignees = convertRoleToUsers(createRequest.getReviewAssignees());
        final var reviewAssignees = changeToAssigneeDtos(workflowRequestReviewAssigneeCompositeService.createCollection(workflowRequest.getId(), requestReviewAssignees, workflowRuleSuite));


        return new WorkflowRequestDto.Response(workflowRequest, approvals, approvalAssignees, executionAssignees, reviewAssignees);
    }

    @Transactional(readOnly = true)
    public WorkflowRequestDto.DetailResponse find(final Long id) {
        final var approvalAssignees = new HashMap<Long, List<AssigneeDto.Response>>();

        final var workflowRequest = compositeService.find(id);

        final var approvals = workflowRequestApprovalCompositeService.findAllByRequestId(workflowRequest.getId());
        approvals.forEach(approval -> approvalAssignees.putAll(approvalAssigneeMap(approval.getId())));
        var executionAssignees = changeToAssigneeDtos(workflowRequestExecutionAssigneeCompositeService.findByRequestId(workflowRequest.getId()));
        final var reviewAssignees = changeToAssigneeDtos(workflowRequestReviewAssigneeCompositeService.findByRequestId(workflowRequest.getId()));

        final var detailAccessControl = workflowRequestDetailAccessControlCompositeService.findByRequestId(workflowRequest.getId());
        final var detailAccessControlConnections = detailAccessControl == null
                ? null
                : workflowRequestDetailAccessControlConnectionCompositeService.findAllByDetailAccessControlId(detailAccessControl.getId());
        final var detailSqlExecution = workflowRequestDetailSqlExecutionCompositeService.findByRequestId(workflowRequest.getId());
        final var detailDataExecution = workflowRequestDetailDataExportCompositeService.findByRequestId(workflowRequest.getId());

        return new WorkflowRequestDto.DetailResponse(workflowRequest, approvals, approvalAssignees, executionAssignees, reviewAssignees,
                detailAccessControl, detailAccessControlConnections, detailSqlExecution, detailDataExecution);
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequestDto.Response> list(final WorkflowRequestType type) {
        final var workflowRequests = compositeService.findAllByType(type);

        final var approvals = workflowRequestApprovalCompositeService.findAll();

        final var mappedApprovalAssignees = new HashMap<Long, HashMap<Long, ArrayList<AssigneeDto.Response>>>();
        workflowRequestApprovalAssigneeCompositeService.findAll()
                .forEach(assignee -> pushInTriple(mappedApprovalAssignees, assignee));

        final var executionAssignees = workflowRequestExecutionAssigneeCompositeService.findAll().stream()
                .collect(Collectors.groupingBy(Assignee::getRequestId));

        final var reviewAssignees = workflowRequestReviewAssigneeCompositeService.findAll().stream()
                .collect(Collectors.groupingBy(Assignee::getRequestId));


        return workflowRequests.stream()
                .map(workflowRequest -> new WorkflowRequestDto.Response(workflowRequest, approvals.get(workflowRequest.getId()),
                        mappedApprovalAssignees.get(workflowRequest.getId()), changeToAssigneeDtos(executionAssignees.get(workflowRequest.getId())),
                        changeToAssigneeDtos(reviewAssignees.get(workflowRequest.getId()))))

                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public WorkflowRequestDto.Cancel.Response cancel(final Long id, final SessionUserId sessionUserId) {
        final var canceled = compositeService.cancel(id, sessionUserId);

        return new WorkflowRequestDto.Cancel.Response(canceled.getType(), canceled.getId(), canceled.getTitle(), canceled.isCanceled());
    }

    @Transactional
    public WorkflowRequestDto.Approve.Response approve(final Long id, final Long order, final SessionUserId sessionUserId) {
        final var updated = compositeService.approve(id, order, sessionUserId);

        return getApproveResponse(id, updated);
    }

    @Transactional
    public WorkflowRequestDto.Approve.Response disApprove(final Long id, final Long order, final SessionUserId sessionUserId) {
        final var updated = compositeService.disApprove(id, order, sessionUserId);

        return getApproveResponse(id, updated);
    }

    @Transactional
    public void execute(final Long workflowRequestId, final SessionUserId sessionUserId) {
        compositeService.execute(workflowRequestId, sessionUserId);
    }

    @Transactional
    public void review(final Long workflowRequestId, final SessionUserId sessionUserId) {
        compositeService.review(workflowRequestId, sessionUserId);
    }

    @Transactional
    public void executeSuccess(final Long workflowRequestId) {
        compositeService.executeSuccess(workflowRequestId);
    }

    @Transactional
    public void executeFail(final Long workflowRequestId) {
        compositeService.executeFail(workflowRequestId);
    }

    @Transactional
    public void executionExpire(final Long id) {
        compositeService.executionExpire(id);
    }

    @Transactional
    public void requestExpire(final Long id) {
        compositeService.requestExpire(id);
    }

    private void validateDetail(final WorkflowRequestDto.Create.Request.Detail detail) {
        final var accessControlConnections = detail.getAccessControl().getConnections();
        final var sqlExecution = detail.getSqlExecution();
        final var dataExport = detail.getDataExport();

        accessControlConnections
                .forEach(connection -> {
                    connectionService.validateExist(connection.getConnectionId());
                    roleService.validateExist(connection.getConnectionId());
                });

        connectionService.validateExist(sqlExecution.getConnectionId());
        connectionService.validateExist(dataExport.getConnectionId());
    }

    private Map<Long, List<Long>> convertApprovalAssigneesToUsers(final List<WorkflowApprovalDto.Create.Request> approvals) {
        final var response = new HashMap<Long, List<Long>>();

        approvals.forEach(e -> response.put(e.getOrder(), convertRoleToUsers(e.getAssignees())));

        return response;

    }

    private List<Long> convertRoleToUsers(final List<AssigneeDto.Request> executionAssignees) {
        return executionAssignees.stream()
                .flatMap(assignee -> {
                    if (assignee.getType() == AssigneeType.ROLE) {
                        roleService.validateExist(assignee.getId());
                        return userRoleService.findUserIdByRole(assignee.getId()).stream();

                    } else {
                        userService.validateExist(assignee.getId());
                        return Stream.of(assignee.getId());

                    }
                })
                .collect(Collectors.toUnmodifiableList());

    }

    private void pushInTriple(final Map<Long, HashMap<Long, ArrayList<AssigneeDto.Response>>> mappedApprovalAssignees, WorkflowRequestApprovalAssignee assignee) {
        final var first = assignee.getRequestId();
        final var second = assignee.getRequestApprovalId();
        final var third = changeToAssigneeDto(assignee);

        if (mappedApprovalAssignees.containsKey(first)) {
            if (mappedApprovalAssignees.get(first).containsKey(second)) {
                mappedApprovalAssignees.get(first).get(second).add(third);

            } else {
                mappedApprovalAssignees.get(first).put(second, new ArrayList<>(List.of(third)));
            }
        } else {
            mappedApprovalAssignees.put(first, new HashMap<>(Map.of(second, new ArrayList<>(List.of(third)))));
        }
    }

    private Map<Long, List<AssigneeDto.Response>> approvalAssigneeMap(final Long approvalId) {
        final var assigneeUser = getAssigneeDtosByApprovalId(approvalId);
        return Map.of(approvalId, assigneeUser);
    }

    private List<AssigneeDto.Response> getAssigneeDtosByApprovalId(final Long approvalId) {
        return changeToAssigneeDtos(workflowRequestApprovalAssigneeCompositeService.findAllByApprovalId(approvalId));
    }

    private List<AssigneeDto.Response> changeToAssigneeDtos(final List<? extends Assignee> assignees) {
        if (assignees == null) {
            return List.of();
        }
        return assignees.stream()
                .map(this::changeToAssigneeDto)
                .collect(Collectors.toUnmodifiableList());
    }

    private AssigneeDto.Response changeToAssigneeDto(final Assignee assignee) {
        return AssigneeDto.Response.of(userService.find(assignee.getAssigneeId()), assignee.getStatus());
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

    private WorkflowRequestDto.Approve.Response getApproveResponse(Long id, WorkflowRequest updated) {
        final var approvalAssignees = new HashMap<Long, List<AssigneeDto.Response>>();

        final var approvals = workflowRequestApprovalCompositeService.findAllByRequestId(id);
        approvals.forEach(approval -> approvalAssignees.putAll(approvalAssigneeMap(approval.getId())));

        return WorkflowRequestDto.Approve.Response.from(updated, approvals, approvalAssignees);
    }
}
