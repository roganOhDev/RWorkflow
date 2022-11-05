package com.source.rworkflow.workflow.domain.approval.assignee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRequestApprovalAssigneeCompositeService {
    private final WorkflowRequestApprovalAssigneeService service;

    @Transactional
    public List<WorkflowRequestApprovalAssignee> createCollection(final List<Long> ids, final Long requestId, final Long approvalId) {
        return ids.stream()
                .map(id -> create(id, requestId, approvalId))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequestApprovalAssignee> findAllByApprovalId(final Long approvalId) {
        return service.findAllByApprovalId(approvalId);
    }

    private WorkflowRequestApprovalAssignee create(final Long id, final Long requestId, final Long approvalId) {
        final var assignee = new WorkflowRequestApprovalAssignee();

        assignee.setAssigneeId(id);
        assignee.setRequestApprovalId(approvalId);
        assignee.setRequestId(requestId);

        return service.create(assignee);
    }

}
