package com.source.rworkflow.workflowRule.domain.rule;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.misc.role.RoleService;
import com.source.rworkflow.misc.user.UserService;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
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
import com.source.rworkflow.workflowRule.exception.ApprovalAssigneeCanNotBeCreatedWhenUrgentException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public WorkflowRuleDto.Response create(final WorkflowRuleDto.Create.Request request, final SessionUserId sessionUserId) {
        final var ruleSuite = new RuleSuite();

        final var created = compositeService.create(request, sessionUserId);

        if (request.getApprovals() != null) {
            request.getApprovals().forEach(approval -> {
                approval.getAssignees().forEach(this::assigneeValidate);
            });

            ruleSuite.setApprovals(workflowRuleApprovalCompositeService.createCollection(created.getId(), request.getApprovals()));

            ruleSuite.getApprovals()
                    .forEach(approval -> {
                        final var assignees = workflowRuleApprovalAssigneeCompositeService.find(approval.getId());
                        ruleSuite.putApprovalAssignees(new HashMap<>(Map.of(approval.getId(), assignees)));
                    });
        }

        request.getExecutionAssignees().forEach(this::assigneeValidate);
        ruleSuite.setExecutionAssignees(workflowRuleExecutionAssigneeCompositeService.createCollection(created.getId(), request.getExecutionAssignees()));

        request.getReviewAssignees().forEach(this::assigneeValidate);
        ruleSuite.setReviewAssignees(workflowRuleReviewAssigneeCompositeService.createCollection(created.getId(), request.getReviewAssignees()));

        return WorkflowRuleDto.Response.from(created, ruleSuite.getApprovals(), ruleSuite.getApprovalAssignees(), ruleSuite.getExecutionAssignees(), ruleSuite.getReviewAssignees());
    }

    public WorkflowRuleDto.Delete.Response delete(final Long id, final SessionUserId sessionUserId) {
        final var workflowRule = service.find(id);
        final var deleted = compositeService.delete(workflowRule, sessionUserId);

        return new WorkflowRuleDto.Delete.Response(deleted.getId(), deleted.getName(), deleted.getRequestType());
    }

    public WorkflowRuleDto.Response update(final WorkflowRuleDto.Update.Request request, final SessionUserId sessionUserId) {
        final var ruleSuite = new RuleSuite();

        final var ruleId = request.getId();
        final var workflowRule = service.find(ruleId);

        final var updated = compositeService.update(workflowRule, request, sessionUserId);

        if (request.getApprovals() != null) {
            request.getApprovals().forEach(approval -> {
                approval.getAssignees().forEach(this::assigneeValidate);
            });

            ruleSuite.setApprovals(workflowRuleApprovalCompositeService.updateCollection(ruleId, request.getApprovals()));

            ruleSuite.getApprovals()
                    .forEach(approval -> {
                        final var assignees = workflowRuleApprovalAssigneeCompositeService.find(approval.getId());
                        ruleSuite.putApprovalAssignees(new HashMap<>(Map.of(approval.getId(), assignees)));
                    });
        }

        final var hasApproval = workflowRuleApprovalCompositeService.findAllByRuleId(ruleId).size() > 0;
        checkUrgentApproval(updated.isUrgent(), hasApproval);

        if (request.getExecutionAssignees() != null) {
            request.getExecutionAssignees().forEach(this::assigneeValidate);
            ruleSuite.setExecutionAssignees(workflowRuleExecutionAssigneeCompositeService.updateCollection(ruleId, request.getExecutionAssignees()));
        }

        if (request.getReviewAssignees() != null) {
            request.getReviewAssignees().forEach(this::assigneeValidate);
            ruleSuite.setReviewAssignees(workflowRuleReviewAssigneeCompositeService.updateCollection(ruleId, request.getReviewAssignees()));
        }

        return WorkflowRuleDto.Response.from(updated, ruleSuite.getApprovals(), ruleSuite.getApprovalAssignees(), ruleSuite.getExecutionAssignees(), ruleSuite.getReviewAssignees());
    }

    public WorkflowRuleDto.Read.Response list(final String workflowRequestTypeString) {
        final var workflowRules = workflowRequestTypeString == null
                ? service.findAll()
                : service.find(WorkflowRequestType.of(workflowRequestTypeString));

        return new WorkflowRuleDto.Read.Response(workflowRules.stream()
                .map(this::createRuleSuiteByRule)
                .collect(Collectors.toUnmodifiableList()));
    }

    public WorkflowRuleDto.Response find(final Long id) {
        final var workflowRule = service.find(id);

        return createRuleSuiteByRule(workflowRule);
    }

    private WorkflowRuleDto.Response createRuleSuiteByRule(final WorkflowRule rule) {
        final var ruleSuite = new RuleSuite();

        ruleSuite.setApprovals(workflowRuleApprovalCompositeService.findAllByRuleId(rule.getId()).stream()
                .peek(approval -> ruleSuite.putApprovalAssignees(new HashMap<>(Map.of(approval.getId(), workflowRuleApprovalAssigneeCompositeService.find(approval.getId())))))
                .collect(Collectors.toUnmodifiableList()));

        ruleSuite.setExecutionAssignees(workflowRuleExecutionAssigneeCompositeService.findAllByRuleId(rule.getId()));

        ruleSuite.setReviewAssignees(workflowRuleReviewAssigneeCompositeService.findAllByRuleId(rule.getId()));

        return WorkflowRuleDto.Response.from(rule, ruleSuite.getApprovals(), ruleSuite.getApprovalAssignees(), ruleSuite.getExecutionAssignees(), ruleSuite.getReviewAssignees());
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

    private void checkUrgentApproval(final boolean isUrgent, final boolean hasApproval) {
        if (isUrgent && hasApproval) {
            throw new ApprovalAssigneeCanNotBeCreatedWhenUrgentException();
        }
    }

    @Getter
    public static class RuleSuite {
        private List<WorkflowRuleApproval> approvals;
        private HashMap<Long, List<WorkflowRuleApprovalAssignee>> approvalAssignees;
        private List<WorkflowRuleExecutionAssignee> executionAssignees;
        private List<WorkflowRuleReviewAssignee> reviewAssignees;

        public RuleSuite() {
            approvals = new ArrayList<>();
            approvalAssignees = new HashMap<>();
            executionAssignees = new ArrayList<>();
            reviewAssignees = new ArrayList<>();
        }

        public void setApprovals(List<WorkflowRuleApproval> approvals) {
            this.approvals = approvals;
        }

        public void putApprovalAssignees(HashMap<Long, List<WorkflowRuleApprovalAssignee>> approvalAssignees) {
            this.approvalAssignees.putAll(approvalAssignees);
        }

        public void setExecutionAssignees(List<WorkflowRuleExecutionAssignee> executionAssignees) {
            this.executionAssignees = executionAssignees;
        }

        public void setReviewAssignees(List<WorkflowRuleReviewAssignee> reviewAssignees) {
            this.reviewAssignees = reviewAssignees;
        }
    }
}
