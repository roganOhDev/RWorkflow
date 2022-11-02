package com.source.rworkflow.workflowRule.domain.executionAssignee;

import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRuleExecutionAssigneeCompositeService {
    private final WorkflowRuleExecutionAssigneeService service;

    public List<WorkflowRuleExecutionAssignee> createCollection(final Long ruleId, final List<AssigneeDto.Request> assignees) {
        return assignees.stream()
                .map(assignee -> create(ruleId, assignee))
                .collect(Collectors.toUnmodifiableList());
    }

    public void deleteCollection(final List<WorkflowRuleExecutionAssignee> assignees) {
        assignees.forEach(this::delete);
    }

    public List<WorkflowRuleExecutionAssignee> findAllByRuleId(final Long ruleId) {
        return service.find(ruleId);
    }

    private WorkflowRuleExecutionAssignee create(final Long ruleId, final AssigneeDto.Request request) {
        final var assignee = new WorkflowRuleExecutionAssignee();

        assignee.setRuleId(ruleId);
        assignee.setAssigneeType(request.getType());
        assignee.setAssigneeValue(request.getValue());

        return service.create(assignee);
    }

    private void delete(final WorkflowRuleExecutionAssignee assignee) {
        service.delete(assignee);
    }
}
