package com.source.rworkflow.workflowRule.domain.rule;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRuleTransferService {
    private WorkflowRuleCompositeService compositeService;

    public WorkflowRuleDto.Create.Response create(final WorkflowRuleDto.Create.Request request, final SessionUserId sessionUserId) {

    }
}
