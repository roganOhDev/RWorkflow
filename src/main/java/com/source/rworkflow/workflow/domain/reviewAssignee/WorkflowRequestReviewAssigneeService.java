package com.source.rworkflow.workflow.domain.reviewAssignee;

import com.source.rworkflow.workflow.type.AssigneeStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRequestReviewAssigneeService {
    private final WorkflowRequestReviewAssigneeRepository repository;

    public WorkflowRequestReviewAssignee create(final WorkflowRequestReviewAssignee assignee) {
        assignee.setStatus(AssigneeStatusType.NONE);

        return repository.save(assignee);
    }

    public List<WorkflowRequestReviewAssignee> findAll() {
        return repository.findAll();
    }

    public List<WorkflowRequestReviewAssignee> findByRequestId(final Long requestId) {
        return repository.findByRequestId(requestId);
    }

    public void updateStatus(final WorkflowRequestReviewAssignee assignee) {
        assignee.setActionAt(LocalDateTime.now());

        repository.save(assignee);
    }
}
