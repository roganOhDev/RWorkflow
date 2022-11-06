package com.source.rworkflow.workflow.domain.approval;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflow.dto.AssigneeDto;
import com.source.rworkflow.workflow.dto.WorkflowApprovalDto;
import com.source.rworkflow.workflow.exception.ApprovalHaveToFollowItsTurnException;
import com.source.rworkflow.workflow.exception.ApprovalNotFoundByOrderException;
import com.source.rworkflow.workflow.exception.AssigneeCanNotBeEmpty;
import com.source.rworkflow.workflow.exception.SelfApproveException;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
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
                                                          final Map<Long, List<Long>> assignees, final WorkflowRuleSuite workflowRuleSuite, final SessionUserId sessionUserId) {
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
    public int approve(final Long requestId, final Long order, final SessionUserId sessionUserId, final boolean approve) {

        final var approvals = findAllByRequestId(requestId);

        final var fitApproval = approvals.stream()
                .filter(approval -> approval.getOrder().equals(order))
                .collect(Collectors.toUnmodifiableList());

        if (fitApproval.size() == 0) {
            throw new ApprovalNotFoundByOrderException(requestId, order);
        }

        final var beforeApproval = approvals.stream()
                .filter(approval -> approval.getOrder().equals(order - 1))
                .findFirst();

        if (beforeApproval.isPresent() && !beforeApproval.get().getStatus().isFinished()) {
            throw new ApprovalHaveToFollowItsTurnException();
        }

        if (approve) {
            triggerService.approveOk(fitApproval.get(0), sessionUserId);
        } else {
            triggerService.approveReject(fitApproval.get(0), sessionUserId);
        }

        return approvals.size();
    }

    private WorkflowRequestApproval create(final Long requestId, final List<Long> assignees, final WorkflowApprovalDto.Create.Request creatRequest) {
        final var workflowRequestApproval = getNewWorkflowRequestApproval(requestId, creatRequest);

        validateAssigneeCount((long) assignees.size());

        return triggerService.create(assignees, requestId, workflowRequestApproval);
    }

    private WorkflowRequestApproval create(final Long requestId, final List<Long> assignees, final WorkflowApprovalDto.Create.Request creatRequest,
                                           final List<Long> assigneesByRule, final SessionUserId sessionUserId) {
        final var workflowRequestApproval = getNewWorkflowRequestApproval(requestId, creatRequest);

        final var mergedAssignees = mergeAssignees(assignees, assigneesByRule, sessionUserId);
        validateAssigneeCount((long) assignees.size());

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

    private List<Long> mergeAssignees(final List<Long> assignees, List<Long> assigneesByRule, SessionUserId sessionUserId) {
        final var ruleAssignees = removeSelfFromAssigneesByRule(assigneesByRule, sessionUserId);

        final var resultList = new ArrayList<>(assignees);
        resultList.addAll(ruleAssignees);
        return ListUtil.removeDuplicateElement(resultList);
    }

    private List<Long> removeSelfFromAssigneesByRule(List<Long> assigneesByRule, SessionUserId sessionUserId) {
        final var mutableAssigneesByRule = new ArrayList<>(assigneesByRule);
        mutableAssigneesByRule.remove(sessionUserId.getId());

        return mutableAssigneesByRule;
    }

    private WorkflowRequestApproval getNewWorkflowRequestApproval(final Long requestId, final WorkflowApprovalDto.Create.Request creatRequest) {
        final var workflowRequestApproval = new WorkflowRequestApproval();

        workflowRequestApproval.setRequestId(requestId);
        workflowRequestApproval.setApproveType(creatRequest.getApproveType());
        workflowRequestApproval.setOrder(creatRequest.getOrder());

        return workflowRequestApproval;
    }
}
