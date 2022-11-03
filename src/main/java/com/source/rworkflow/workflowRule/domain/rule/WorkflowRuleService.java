package com.source.rworkflow.workflowRule.domain.rule;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import com.source.rworkflow.workflowRule.exception.WorkflowRuleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkflowRuleService {
    private final WorkflowRuleRepository repository;

    public WorkflowRule create(final WorkflowRule workflowRule, final SessionUserId sessionUserId) {
        final var now = LocalDateTime.now();

        workflowRule.setCreatedAt(now);
        workflowRule.setCreatedBy(sessionUserId.getId());
        workflowRule.setUpdatedAt(now);
        workflowRule.setUpdatedBy(sessionUserId.getId());

        return repository.save(workflowRule);
    }

    public WorkflowRule delete(final WorkflowRule workflowRule, final SessionUserId sessionUserId) {
        final var now = LocalDateTime.now();

        workflowRule.setUpdatedAt(now);
        workflowRule.setUpdatedBy(sessionUserId.getId());

        return repository.save(workflowRule);
    }

    public List<WorkflowRule> find(final WorkflowRequestType type) {
        return repository.findAllByRequestTypeAndDeletedIsFalse(type);
    }

    public List<WorkflowRule> findAll() {
        return repository.findAllByDeletedIsFalse();
    }

    public WorkflowRule find(final Long id) {
        final var workflowRule = repository.findByIdAndDeletedIsFalse(id);

        if (workflowRule == null) {
            throw new WorkflowRuleNotFoundException(id);
        }

        return workflowRule;
    }

    public WorkflowRule update(final WorkflowRule workflowRule, final SessionUserId sessionUserId) {
        final var now = LocalDateTime.now();

        workflowRule.setUpdatedAt(now);
        workflowRule.setUpdatedBy(sessionUserId.getId());

        return repository.save(workflowRule);
    }
}
