package com.source.rworkflow.workflow.domain.executeAssignee;

import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssignee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRequestExecutionAssigneeCompositeService {
    private final WorkflowRequestExecutionAssigneeService service;

    @Transactional
    public List<WorkflowRequestExecutionAssignee> createCollection(final Long requestId, final List<Long> executeAssignees,
                                                                   final WorkflowRuleSuite workflowRuleSuite) {
        var assignees = executeAssignees;

        if (workflowRuleSuite != null) {
            assignees = mergeAssignees(workflowRuleSuite, executeAssignees);
        }

        final var resultList = assignees.stream()
                .map(assignee -> create(requestId, assignee))
                .collect(Collectors.toUnmodifiableList());

        return ListUtil.removeDuplicateElement(resultList);
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequestExecutionAssignee> findAll() {
        return service.findAll();
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequestExecutionAssignee> findByRequestId(final Long requestId) {
        return service.findByRequestId(requestId);
    }

    private WorkflowRequestExecutionAssignee create(final Long requestId, final Long createRequest) {
        final var assignee = new WorkflowRequestExecutionAssignee();

        assignee.setRequestId(requestId);
        assignee.setAssigneeId(createRequest);

        return service.create(assignee);
    }

    private List<Long> mergeAssignees(final WorkflowRuleSuite workflowRuleSuite, List<Long> executionAssignees) {
        final var ruleAssignees = new ArrayList<>(workflowRuleSuite.getExecutionAssignees().stream()
                .map(WorkflowRuleExecutionAssignee::getAssigneeValue)
                .collect(Collectors.toUnmodifiableList()));

        ruleAssignees.addAll(executionAssignees);

        return ListUtil.removeDuplicateElement(ruleAssignees);
    }
}
