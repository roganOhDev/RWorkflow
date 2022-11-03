package com.source.rworkflow.workflowRule.domain.executionAssignee;

import com.source.rworkflow.common.util.Comparer;
import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateAssigneeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRuleExecutionAssigneeCompositeService {
    private final WorkflowRuleExecutionAssigneeService service;

    @Transactional
    public List<WorkflowRuleExecutionAssignee> createCollection(final Long ruleId, final List<AssigneeDto.Request> assignees) {
        return assignees.stream()
                .map(assignee -> create(ruleId, assignee))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void deleteCollection(final List<WorkflowRuleExecutionAssignee> assignees) {
        assignees.forEach(this::delete);
    }

    @Transactional
    public List<WorkflowRuleExecutionAssignee> updateCollection(final Long ruleId, List<AssigneeDto.Request> requests) {
        final var existingAssignees = findAllByRuleId(ruleId);

        final var results = Comparer.compare(existingAssignees, requests, (approval, request) -> approval.getId().equals(request.getId()));

        final var assginees = results.execute(
                createRequest -> this.create(ruleId, createRequest),
                this::delete
        );

        if(ListUtil.hasDuplicateElement(assginees)) {
            throw new CanNotDuplicateAssigneeException();
        };

        return assginees;
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
