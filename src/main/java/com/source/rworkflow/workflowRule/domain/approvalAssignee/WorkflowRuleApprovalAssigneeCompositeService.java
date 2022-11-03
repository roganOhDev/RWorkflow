package com.source.rworkflow.workflowRule.domain.approvalAssignee;

import com.source.rworkflow.common.util.Comparer;
import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import com.source.rworkflow.workflowRule.exception.CanNotDuplicateAssigneeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRuleApprovalAssigneeCompositeService {
    private final WorkflowRuleApprovalAssigneeService service;

    @Transactional
    public List<WorkflowRuleApprovalAssignee> createCollection(final Long approvalId, final List<AssigneeDto.Request> requests) {
        final var existingAssignees = service.find(approvalId);

        final var results = Comparer.compare(existingAssignees, requests, (approval, request) -> approval.getId().equals(request.getId()));

        final var assginees = results.execute(
                createRequest -> this.create(approvalId, createRequest),
                this::delete
        );

        if(ListUtil.hasDuplicateElement(assginees)) {
            throw new CanNotDuplicateAssigneeException();
        };

        return assginees;
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
