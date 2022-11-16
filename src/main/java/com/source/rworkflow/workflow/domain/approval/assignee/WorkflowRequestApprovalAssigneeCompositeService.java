package com.source.rworkflow.workflow.domain.approval.assignee;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.domain.approval.WorkflowRequestApproval;
import com.source.rworkflow.workflow.exception.ApprovalAssigneeNotFoundException;
import com.source.rworkflow.workflow.exception.AssigneeCanNotAction;
import com.source.rworkflow.workflow.type.AssigneeStatusType;
import com.source.rworkflow.workflowRule.type.ApproveType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRequestApprovalAssigneeCompositeService {
    private final WorkflowRequestApprovalAssigneeService service;

    @Transactional
    public List<WorkflowRequestApprovalAssignee> createCollection(final List<Long> assignees, final Long requestId, final Long approvalId) {
        return assignees.stream()
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
    public WorkflowRequestApprovalAssignee createUrgent(final Long requestId, final Long approvalId) {
        return createUrgentAssignee(requestId, approvalId);
    }

    @Transactional
    public boolean approve(final WorkflowRequestApproval approval, final SessionUserId sessionUserId) {
        final var assignees = service.findAllByApprovalId(approval.getId());

        final WorkflowRequestApprovalAssignee currentAssignee = getCurrentAssignee(approval, sessionUserId, assignees);

        currentAssignee.setStatus(AssigneeStatusType.APPROVED);
        service.changeApproveStatus(currentAssignee, sessionUserId);

        if (approval.getApproveType().equals(ApproveType.ANY)) {
            return true;
        }
        return assignees.stream()
                .filter(assignee -> assignee.getStatus().equals(AssigneeStatusType.APPROVED))
                .count() == assignees.size() - 1;
    }

    @Transactional
    public void disApprove(final WorkflowRequestApproval approval, final SessionUserId sessionUserId) {
        final var assignees = service.findAllByApprovalId(approval.getId());

        final WorkflowRequestApprovalAssignee currentAssignee = getCurrentAssignee(approval, sessionUserId, assignees);

        currentAssignee.setStatus(AssigneeStatusType.REJECTED);
        service.changeApproveStatus(currentAssignee, sessionUserId);
    }

    private WorkflowRequestApprovalAssignee getCurrentAssignee(WorkflowRequestApproval approval, SessionUserId sessionUserId, List<WorkflowRequestApprovalAssignee> assignees) {
        final var currentAssignee = assignees.stream()
                .peek(this::validateStatus)
                .filter(assignee -> assignee.getAssigneeId().equals(sessionUserId.getId()))
                .findFirst().orElseThrow(() -> new ApprovalAssigneeNotFoundException(approval.getRequestId(), approval.getId(), approval.getOrder(), sessionUserId.getId()));

        return currentAssignee;
    }

    private void validateStatus(final WorkflowRequestApprovalAssignee assignee) {
        if (assignee.getStatus().equals(AssigneeStatusType.NONE) || assignee.getStatus().equals(AssigneeStatusType.PENDING)) {
            return;
        }

        throw new AssigneeCanNotAction("approve", assignee.getStatus());
    }

    private WorkflowRequestApprovalAssignee create(final Long id, final Long requestId, final Long approvalId) {
        final var assignee = new WorkflowRequestApprovalAssignee();

        assignee.setAssigneeId(id);
        assignee.setRequestApprovalId(approvalId);
        assignee.setRequestId(requestId);

        return service.create(assignee);
    }

    private WorkflowRequestApprovalAssignee createUrgentAssignee(final Long requestId, final Long approvalId) {
        final var assignee = new WorkflowRequestApprovalAssignee();

        assignee.setAssigneeId(1L);
        assignee.setRequestApprovalId(approvalId);
        assignee.setRequestId(requestId);
        assignee.setStatus(AssigneeStatusType.APPROVED);
        assignee.setActionAt(LocalDateTime.now());
        assignee.setActionBy(1L);

        return service.createUrgent(assignee);
    }

}
