package com.source.rworkflow.workflowRule.domain.rule;

import com.source.rworkflow.common.domain.SessionUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRuleTriggerService {
    private final WorkflowRuleTrigger trigger;
    private final WorkflowRuleService service;

    public WorkflowRule delete(final WorkflowRule workflowRule, final SessionUserId sessionUserId) {
        trigger.beforeDelete(workflowRule);

        return service.delete(workflowRule, sessionUserId);
    }
}
