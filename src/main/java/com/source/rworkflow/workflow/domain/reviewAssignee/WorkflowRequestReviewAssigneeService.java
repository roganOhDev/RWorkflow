package com.source.rworkflow.workflow.domain.reviewAssignee;

import com.source.rworkflow.workflow.type.AssigneeStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestReviewAssigneeService {
    private final WorkflowRequestReviewAssigneeRepository repository;

    public WorkflowRequestReviewAssignee create(final WorkflowRequestReviewAssignee assignee) {
        assignee.setStatus(AssigneeStatusType.None);

        return repository.save(assignee);
    }
}
