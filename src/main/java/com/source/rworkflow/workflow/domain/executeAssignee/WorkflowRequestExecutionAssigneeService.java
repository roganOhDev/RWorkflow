package com.source.rworkflow.workflow.domain.executeAssignee;

import com.source.rworkflow.workflow.type.AssigneeStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestExecutionAssigneeService {
    private final WorkflowRequestExecutionAssigneeRepository repository;

    public WorkflowRequestExecutionAssignee create(final WorkflowRequestExecutionAssignee assignee) {
        assignee.setStatus(AssigneeStatusType.None);

        return repository.save(assignee);
    }
}
