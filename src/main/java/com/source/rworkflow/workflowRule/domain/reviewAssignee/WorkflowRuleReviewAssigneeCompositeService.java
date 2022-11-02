package com.source.rworkflow.workflowRule.domain.reviewAssignee;

import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRuleReviewAssigneeCompositeService {
    private final WorkflowRuleReviewAssigneeService service;

    @Transactional
    public List<WorkflowRuleReviewAssignee> createCollection(final Long ruleId, final List<AssigneeDto.Request> assignees) {
        return assignees.stream()
                .map(assignee -> create(ruleId, assignee))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void deleteCollection(final List<WorkflowRuleReviewAssignee> assignees) {
        assignees.forEach(this::delete);
    }

    @Transactional(readOnly = true)
    public List<WorkflowRuleReviewAssignee> findAllByRuleId(final Long ruleId) {
        return service.findAllByRuleId(ruleId);
    }

    private WorkflowRuleReviewAssignee create(final Long ruleId, final AssigneeDto.Request request) {
        final var assignee = new WorkflowRuleReviewAssignee();

        assignee.setRuleId(ruleId);
        assignee.setAssigneeType(request.getType());
        assignee.setAssigneeValue(request.getValue());

        return service.create(assignee);
    }

    private void delete(final WorkflowRuleReviewAssignee assignee) {
        service.delete(assignee);
    }
}
