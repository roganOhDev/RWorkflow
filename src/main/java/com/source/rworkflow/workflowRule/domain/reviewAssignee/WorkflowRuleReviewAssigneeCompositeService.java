package com.source.rworkflow.workflowRule.domain.reviewAssignee;

import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRuleReviewAssigneeCompositeService {
    private final WorkflowRuleReviewAssigneeService service;

    public List<WorkflowRuleReviewAssignee> createCollection(final Long ruleId, final List<AssigneeDto.Request> assignees) {
        return assignees.stream()
                .map(assignee -> create(ruleId, assignee))
                .collect(Collectors.toUnmodifiableList());
    }

    private WorkflowRuleReviewAssignee create(final Long ruleId, final AssigneeDto.Request request) {
        final var assignee = new WorkflowRuleReviewAssignee();

        assignee.setRuleId(ruleId);
        assignee.setAssigneeType(request.getType());
        assignee.setAssigneeValue(request.getValue());

        return service.create(assignee);
    }
}
