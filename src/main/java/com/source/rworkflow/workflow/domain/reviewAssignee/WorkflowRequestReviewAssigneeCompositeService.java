package com.source.rworkflow.workflow.domain.reviewAssignee;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflow.exception.AssigneeCanNotAction;
import com.source.rworkflow.workflow.exception.AssigneeCanNotBeEmpty;
import com.source.rworkflow.workflow.exception.ReviewAssigneeNotFoundException;
import com.source.rworkflow.workflow.type.AssigneeStatusType;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssignee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRequestReviewAssigneeCompositeService {
    private final WorkflowRequestReviewAssigneeService service;

    @Transactional
    public List<WorkflowRequestReviewAssignee> createCollection(final Long requestId, final List<Long> reviewAssignees, final WorkflowRuleSuite workflowRuleSuite) {
        var assignees = reviewAssignees;

        if (workflowRuleSuite != null) {
            assignees = mergeAssignees(workflowRuleSuite, reviewAssignees);
        }

        validateAssigneeCount((long) assignees.size());

        final var resultList = assignees.stream()
                .map(assignee -> create(requestId, assignee))
                .collect(Collectors.toUnmodifiableList());

        return ListUtil.removeDuplicateElement(resultList);
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequestReviewAssignee> findAll() {
        return service.findAll();
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequestReviewAssignee> findByRequestId(final Long requestId) {
        return service.findByRequestId(requestId);
    }


    @Transactional
    public boolean review(final Long requestId, final SessionUserId sessionUserId) {
        final var executionAssignees = service.findByRequestId(requestId);
        final var requestAssignee = executionAssignees.stream()
                .filter(e -> e.getAssigneeId().equals(sessionUserId.getId()))
                .findFirst().orElseThrow(() -> {
                    throw new ReviewAssigneeNotFoundException(sessionUserId.getId());
                });

        if (!requestAssignee.getStatus().equals(AssigneeStatusType.PENDING)) {
            throw new AssigneeCanNotAction("review", requestAssignee.getStatus());
        }

        requestAssignee.setStatus(AssigneeStatusType.APPROVED);
        requestAssignee.setActionBy(sessionUserId.getId());
        requestAssignee.setActionAt(LocalDateTime.now());

        service.updateStatus(requestAssignee);

        final var approvedAssigneesCount = executionAssignees.stream()
                .filter(e -> e.getStatus().equals(AssigneeStatusType.APPROVED))
                .count();

        return executionAssignees.size() - 1 == approvedAssigneesCount;
    }

    @Transactional()
    public void makeReviewAssigneesPending(final Long requestId, final Long executeUserId) {
        final var assignees = service.findByRequestId(requestId);

        assignees
                .forEach(assignee -> {
                    assignee.setStatus(AssigneeStatusType.PENDING);
                    assignee.setActionAt(LocalDateTime.now());
                    assignee.setActionBy(executeUserId);

                    service.updateStatus(assignee);
                });

    }

    private void validateAssigneeCount(final Long size) {
        if (size == 0) {
            throw new AssigneeCanNotBeEmpty("Review");
        }
    }

    private WorkflowRequestReviewAssignee create(final Long requestId, final Long createRequest) {
        final var assignee = new WorkflowRequestReviewAssignee();

        assignee.setRequestId(requestId);
        assignee.setAssigneeId(createRequest);

        return service.create(assignee);
    }

    private List<Long> mergeAssignees(final WorkflowRuleSuite workflowRuleSuite, List<Long> reviewAssignees) {
        final var ruleAssignees = new ArrayList<>(workflowRuleSuite.getExecutionAssignees().stream()
                .map(WorkflowRuleExecutionAssignee::getAssigneeValue)
                .collect(Collectors.toUnmodifiableList()));

        ruleAssignees.addAll(reviewAssignees);

        return ListUtil.removeDuplicateElement(ruleAssignees);
    }
}
