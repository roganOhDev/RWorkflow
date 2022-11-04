package com.source.rworkflow.workflow.controller;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.misc.user.UserDto;
import com.source.rworkflow.misc.user.UserService;
import com.source.rworkflow.workflow.domain.Assignee;
import com.source.rworkflow.workflow.domain.approval.WorkflowRequestApprovalCompositeService;
import com.source.rworkflow.workflow.domain.approval.assignee.WorkflowRequestApprovalAssigneeCompositeService;
import com.source.rworkflow.workflow.domain.executeAssignee.WorkflowRequestExecutionAssigneeCompositeService;
import com.source.rworkflow.workflow.domain.request.WorkflowRequestCompositeService;
import com.source.rworkflow.workflow.domain.reviewAssignee.WorkflowRequestReviewAssigneeCompositeService;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import com.source.rworkflow.workflowRule.WorkflowRuleTransferService;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuiteFactory;
import com.source.rworkflow.workflowRule.domain.rule.WorkflowRuleCompositeService;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    private final UserService userService;
    private final WorkflowRuleCompositeService workflowRuleCompositeService;
    private final WorkflowRuleSuiteFactory workflowRuleSuiteFactory;

    public WorkflowRequestDto.Create.Response create(final WorkflowRequestDto.Create.Request createRequest, final SessionUserId sessionUserId) {
        WorkflowRuleSuite workflowRuleSuite = null;
        final var approvalAssignees = new HashMap<Long, List<UserDto>>();

        if (createRequest.getRuleId() != null) {
            final var workflowRule = workflowRuleCompositeService.find(createRequest.getRuleId());
            workflowRuleSuite = workflowRuleSuiteFactory.of(workflowRule);
        }

        final var workflowRequest = compositeService.create(createRequest, sessionUserId, workflowRuleSuite);

        final var approvals = workflowRequestApprovalCompositeService.createCollection(workflowRequest.getId(), createRequest.getApprovals(), workflowRuleSuite);
        approvals.forEach(approval -> approvalAssignees.putAll(approvalAssigneeMap(approval.getId())));

        final var executionAssignees = getApprovalAssigneeUser(workflowRequestExecutionAssigneeCompositeService.createCollection(workflowRequest.getId(), createRequest.getExecutionAssignees(), workflowRuleSuite));
        final var reviewAssignees = getApprovalAssigneeUser(workflowRequestReviewAssigneeCompositeService.createCollection(workflowRequest.getId(), createRequest.getReviewAssignees(), workflowRuleSuite));


        return WorkflowRequestDto.Create.Response.from(workflowRequest, approvals, approvalAssignees, executionAssignees, reviewAssignees);
    }

    private Map<Long, List<UserDto>> approvalAssigneeMap(final Long approvalId) {
        final var assigneeUser = getApprovalAssigneeUser(approvalId);
        return Map.of(approvalId, assigneeUser);
    }

    private List<UserDto> getApprovalAssigneeUser(final Long approvalId) {
        return getApprovalAssigneeUser(workflowRequestApprovalAssigneeCompositeService.findAllByApprovalId(approvalId));
    }

    private List<UserDto> getApprovalAssigneeUser(final List<? extends Assignee> assignees) {
        return assignees.stream()
                .map(this::getAssigneeUserDto)
                .collect(Collectors.toUnmodifiableList());
    }

    private UserDto getAssigneeUserDto(final Assignee assignee) {
        return UserDto.of(userService.find(assignee.getAssigneeId()));
    }
}
