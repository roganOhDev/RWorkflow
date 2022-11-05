package com.source.rworkflow.workflow.domain.reviewAssignee;

import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflow.domain.executeAssignee.WorkflowRequestExecutionAssignee;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRequestReviewAssigneeCompositeService {
    private final WorkflowRequestReviewAssigneeService service;

    @Transactional
    public List<WorkflowRequestReviewAssignee> createCollection(final Long requestId, final List<Long> createRequests, final WorkflowRuleSuite workflowRuleSuite) {
        final var assignees = new ArrayList<>(createRequests);

        if (workflowRuleSuite != null) {
            assignees.addAll(workflowRuleSuite.getExecutionAssigneeIds());
        }

        final var resultList = assignees.stream()
                .map(assignee -> create(requestId, assignee))
                .collect(Collectors.toUnmodifiableList());

        return ListUtil.removeDuplicateElement(resultList);
    }

    private WorkflowRequestReviewAssignee create(final Long requestId, final Long createRequest) {
        final var assignee = new WorkflowRequestReviewAssignee();

        assignee.setRequestId(requestId);
        assignee.setAssigneeId(createRequest);

        return service.create(assignee);
    }
}
