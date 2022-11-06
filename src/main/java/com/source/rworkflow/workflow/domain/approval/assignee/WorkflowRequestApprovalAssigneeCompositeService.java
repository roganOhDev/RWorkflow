package com.source.rworkflow.workflow.domain.approval.assignee;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.domain.approval.WorkflowRequestApproval;
import com.source.rworkflow.workflow.exception.ApprovalAssigneeNotFoundException;
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

    @Transactional(readOnly = true)
    public List<WorkflowRequestApprovalAssignee> findAll() {
        return service.findAll();
    }

    @Transactional
    public WorkflowRequestApprovalAssignee approve(final WorkflowRequestApproval approval, final SessionUserId sessionUserId, final boolean approve) {
        final var assignees = service.findAllByApprovalId(approval.getId());

        final var fitAssignee = assignees.stream()
                .filter(assignee -> assignee.getAssigneeId().equals(sessionUserId.getId()))
                .findFirst().orElseThrow(() -> new ApprovalAssigneeNotFoundException(approval.getRequestId(), approval.getId(), approval.getOrder(), sessionUserId.getId()));

        return service.approve(fitAssignee, sessionUserId, approve);
    }

    private WorkflowRequestApprovalAssignee create(final Long id, final Long requestId, final Long approvalId) {
        final var assignee = new WorkflowRequestApprovalAssignee();

        assignee.setAssigneeId(id);
        assignee.setRequestApprovalId(approvalId);
        assignee.setRequestId(requestId);

        return service.create(assignee);
    }

}
