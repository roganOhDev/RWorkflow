package com.source.rworkflow.workflowRule.domain.rule;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.misc.role.RoleService;
import com.source.rworkflow.misc.user.UserService;
import com.source.rworkflow.workflowRule.domain.approval.WorkflowRuleApproval;
import com.source.rworkflow.workflowRule.domain.approval.WorkflowRuleApprovalCompositeService;
import com.source.rworkflow.workflowRule.domain.approvalAssignee.WorkflowRuleApprovalAssignee;
import com.source.rworkflow.workflowRule.domain.approvalAssignee.WorkflowRuleApprovalAssigneeCompositeService;
import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssignee;
import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssigneeCompositeService;
import com.source.rworkflow.workflowRule.domain.reviewAssignee.WorkflowRuleReviewAssignee;
import com.source.rworkflow.workflowRule.domain.reviewAssignee.WorkflowRuleReviewAssigneeCompositeService;
import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkflowRuleTransferService {
    private final WorkflowRuleCompositeService compositeService;
    private final WorkflowRuleService service;
    private final WorkflowRuleApprovalCompositeService workflowRuleApprovalCompositeService;
    private final WorkflowRuleApprovalAssigneeCompositeService workflowRuleApprovalAssigneeCompositeService;
    private final WorkflowRuleExecutionAssigneeCompositeService workflowRuleExecutionAssigneeCompositeService;
    private final WorkflowRuleReviewAssigneeCompositeService workflowRuleReviewAssigneeCompositeService;
    private final UserService userService;
    private final RoleService roleService;

    @Transactional
    public WorkflowRuleDto.Create.Response create(final WorkflowRuleDto.Create.Request request, final SessionUserId sessionUserId) {
        List<WorkflowRuleApproval> createdApprovals = List.of();
        Map<Long, List<WorkflowRuleApprovalAssignee>> createdApprovalAssignees = new HashMap<>(Map.of());
        List<WorkflowRuleExecutionAssignee> createdExecutions = List.of();
        List<WorkflowRuleReviewAssignee> createdReviews = List.of();

        final var created = compositeService.create(request, sessionUserId);

        if (request.getApprovals() != null) {
            request.getApprovals().forEach(approval -> {
                approval.getAssignees().forEach(this::assigneeValidate);
            });

            createdApprovals = workflowRuleApprovalCompositeService.createCollection(created.getId(), request.getApprovals());

            createdApprovals
                    .forEach(approval -> {
                        final var assignees = workflowRuleApprovalAssigneeCompositeService.find(approval.getId());
                        createdApprovalAssignees.put(approval.getId(), assignees);
                    });
        }

        if (request.getExecutions() != null) {
            request.getExecutions().forEach(this::assigneeValidate);

            createdExecutions = workflowRuleExecutionAssigneeCompositeService.createCollection(created.getId(), request.getExecutions());
        }

        if (request.getReviews() != null) {
            request.getReviews().forEach(this::assigneeValidate);

            createdReviews = workflowRuleReviewAssigneeCompositeService.createCollection(created.getId(), request.getExecutions());
        }

        return WorkflowRuleDto.Create.Response.from(created, createdApprovals, createdApprovalAssignees, createdExecutions, createdReviews);
    }

    public WorkflowRuleDto.Delete.Response delete(final Long id, final SessionUserId sessionUserId) {
        final var workflowRule = service.find(id);
        final var deleted = compositeService.delete(workflowRule, sessionUserId);

        return new WorkflowRuleDto.Delete.Response(deleted.getId(), deleted.getName(), deleted.getRequestType());
    }

    public WorkflowRuleDto.Update.Response update(final Long id, final WorkflowRuleDto.Update.Request request, final SessionUserId sessionUserId) {
        final var workflowRule = service.find(id);

        final var updated = compositeService.update(workflowRule, request, sessionUserId);

        return null;
    }

    private void assigneeValidate(final AssigneeDto.Request request) {
        switch (request.getType()) {
            case USER:
                userService.validateExist(request.getValue());
                break;
            case ROLE:
                roleService.validateExist(request.getValue());
                break;
        }
    }
}
