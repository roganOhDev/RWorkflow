package com.source.rworkflow.workflow.domain.approval;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflow.dto.AssigneeDto;
import com.source.rworkflow.workflow.dto.WorkflowApprovalDto;
import com.source.rworkflow.workflow.exception.ApprovalHaveToFollowItsTurnException;
import com.source.rworkflow.workflow.exception.ApprovalNotFoundByOrderException;
import com.source.rworkflow.workflow.exception.AssigneeCanNotBeEmpty;
import com.source.rworkflow.workflow.exception.SelfApproveException;
import com.source.rworkflow.workflow.type.ApprovalStatusType;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
import com.source.rworkflow.workflowRule.type.ApproveType;
import com.source.rworkflow.workflowRule.type.AssigneeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRequestApprovalCompositeService {
    private final WorkflowRequestApprovalTriggerService triggerService;
    private final WorkflowRequestApprovalService service;

    @Transactional
    public List<WorkflowRequestApproval> createCollection(final Long requestId, final List<WorkflowApprovalDto.Create.Request> createRequests,
                                                          final Map<Long, List<Long>> assignees, final WorkflowRuleSuite workflowRuleSuite, final boolean urgent,
                                                          final SessionUserId sessionUserId) {
        if (urgent) {
            return List.of(createUrgent(requestId));
        }

        return createRequests.stream()
                .map(createRequest -> {
                    validateSelfApprove(createRequest.getAssignees(), sessionUserId);

                    return workflowRuleSuite == null
                            ? create(requestId, assignees.get(createRequest.getOrder()), createRequest)
                            : create(requestId, assignees.get(createRequest.getOrder()), createRequest, workflowRuleSuite.findAllApprovalAssigneeByOrder(createRequest.getOrder()), sessionUserId);

                })
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public Map<Long, List<WorkflowRequestApproval>> findAll() {
        return service.findAll().stream()
                .collect(Collectors.groupingBy(WorkflowRequestApproval::getRequestId));
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequestApproval> findAllByRequestId(final Long requestId) {
        return service.findAllByRequestId(requestId);
    }

    @Transactional
    public boolean approve(final Long requestId, final Long order, final SessionUserId sessionUserId) {
        final var approvals = findAllByRequestId(requestId);

        final var currentOrderApproval = getCurrentOrderApproval(requestId, order, approvals);

        triggerService.approve(currentOrderApproval, sessionUserId);

        return approvals.stream()
                .filter(e -> e.getStatus().isApproved())
                .count() == approvals.size() - 1;
    }

    @Transactional
    public void disApprove(final Long requestId, final Long order, final SessionUserId sessionUserId) {
        final var approvals = findAllByRequestId(requestId);

        final var currentOrderApproval = getCurrentOrderApproval(requestId, order, approvals);

        triggerService.disApprove(currentOrderApproval, sessionUserId);
    }

    private static WorkflowRequestApproval getCurrentOrderApproval(Long requestId, Long order, List<WorkflowRequestApproval> approvals) {
        validateApprovalReady(order, approvals);

        final var currentOrderApproval = approvals.stream()
                .filter(approval -> approval.getOrder().equals(order))
                .findFirst().orElseThrow(() -> new ApprovalNotFoundByOrderException(requestId, order));

        return currentOrderApproval;
    }

    private static void validateApprovalReady(Long order, List<WorkflowRequestApproval> approvals) {
        final var previousOrderApproval = approvals.stream()
                .filter(approval -> approval.getOrder().equals(order - 1))
                .findFirst();

        if (previousOrderApproval.isPresent() && !previousOrderApproval.get().getStatus().equals(ApprovalStatusType.APPROVED)) {
            throw new ApprovalHaveToFollowItsTurnException();
        }
    }

    private WorkflowRequestApproval createUrgent(final Long requestId) {
        final var workflowRequestApproval = createNewUrgentWorkflowRequestApproval(requestId);

        return triggerService.createUrgent(requestId, workflowRequestApproval);
    }

    private WorkflowRequestApproval create(final Long requestId, final List<Long> assignees, final WorkflowApprovalDto.Create.Request creatRequest) {
        final var workflowRequestApproval = createNewWorkflowRequestApproval(requestId, creatRequest);

        final var assigneeList = ListUtil.removeDuplicateElement(assignees);
        validateAssigneeCount((long) assigneeList.size());

        return triggerService.create(assigneeList, requestId, workflowRequestApproval);
    }

    private WorkflowRequestApproval create(final Long requestId, final List<Long> assignees, final WorkflowApprovalDto.Create.Request creatRequest,
                                           final List<Long> ruleAssignees, final SessionUserId sessionUserId) {
        final var workflowRequestApproval = createNewWorkflowRequestApproval(requestId, creatRequest);

        final var assigneeList = ListUtil.removeDuplicateElement(assignees);
        final var mergedAssignees = mergeAssignees(assigneeList, ruleAssignees, sessionUserId);
        validateAssigneeCount((long) mergedAssignees.size());

        return triggerService.create(mergedAssignees, requestId, workflowRequestApproval);
    }

    private void validateAssigneeCount(final Long size) {
        if (size == 0) {
            throw new AssigneeCanNotBeEmpty("Approval");
        }
    }

    private void validateSelfApprove(final List<AssigneeDto.Request> assignees, final SessionUserId sessionUserId) {
        final var requests = assignees.stream()
                .filter(request -> request.getType() == AssigneeType.USER)
                .map(AssigneeDto.Request::getId)
                .collect(Collectors.toUnmodifiableList());

        if (requests.contains(sessionUserId.getId())) {
            throw new SelfApproveException(sessionUserId);
        }
    }

    private List<Long> mergeAssignees(final List<Long> assignees, List<Long> ruleAssignees, SessionUserId sessionUserId) {
        final var refinedRuleAssignees = removeSelfFromAssigneesByRule(ruleAssignees, sessionUserId);

        final var resultList = new ArrayList<>(assignees);
        resultList.addAll(refinedRuleAssignees);
        return ListUtil.removeDuplicateElement(resultList);
    }

    private List<Long> removeSelfFromAssigneesByRule(List<Long> assigneesByRule, SessionUserId sessionUserId) {
        final var mutableAssigneesByRule = new ArrayList<>(assigneesByRule);
        mutableAssigneesByRule.remove(sessionUserId.getId());

        return mutableAssigneesByRule;
    }

    private WorkflowRequestApproval createNewUrgentWorkflowRequestApproval(final Long requestId) {
        final var workflowRequestApproval = new WorkflowRequestApproval();

        workflowRequestApproval.setRequestId(requestId);
        workflowRequestApproval.setApproveType(ApproveType.ALL);
        workflowRequestApproval.setOrder(1L);
        workflowRequestApproval.setStatus(ApprovalStatusType.APPROVED);

        return workflowRequestApproval;
    }

    private WorkflowRequestApproval createNewWorkflowRequestApproval(final Long requestId, final WorkflowApprovalDto.Create.Request creatRequest) {
        final var workflowRequestApproval = new WorkflowRequestApproval();

        workflowRequestApproval.setRequestId(requestId);
        workflowRequestApproval.setApproveType(creatRequest.getApproveType());
        workflowRequestApproval.setOrder(creatRequest.getOrder());

        return workflowRequestApproval;
    }
}
