package com.source.rworkflow.workflowRule.domain.approvalAssignee;

import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRuleApprovalAssigneeCompositeService {
    private final WorkflowRuleApprovalAssigneeService service;

    @Transactional
    public List<WorkflowRuleApprovalAssignee> createCollection(final Long approvalId, final List<AssigneeDto.Request> assignees) {
        return assignees.stream()
                .map(assignee -> create(approvalId, assignee))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void deleteCollection(final List<WorkflowRuleApprovalAssignee> assignees) {
        assignees.forEach(this::delete);
    }

    @Transactional(readOnly = true)
    public  List<WorkflowRuleApprovalAssignee> find(final Long approvalId) {
        return service.find(approvalId);
    }


    private WorkflowRuleApprovalAssignee create(final Long approvalId, final AssigneeDto.Request request) {
        final var assignee = new WorkflowRuleApprovalAssignee();

        assignee.setRuleApprovalId(approvalId);
        assignee.setAssigneeType(request.getType());
        assignee.setAssigneeValue(request.getValue());

        return service.create(assignee);
    }

    public void delete(final WorkflowRuleApprovalAssignee assignee) {
        service.delete(assignee);
    }
}
