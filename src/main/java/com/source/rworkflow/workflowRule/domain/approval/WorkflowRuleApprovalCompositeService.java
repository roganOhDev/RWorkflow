package com.source.rworkflow.workflowRule.domain.approval;

import com.source.rworkflow.workflowRule.domain.approvalAssignee.WorkflowRuleApprovalAssignee;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleApprovalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRuleApprovalCompositeService {
    private final WorkflowRuleApprovalTriggerService triggerService;

    public List<WorkflowRuleApproval> createCollection(final Long ruleId, final List<WorkflowRuleApprovalDto.Request> requests) {
        return requests.stream()
                .map(request -> create(ruleId, request))
                .collect(Collectors.toUnmodifiableList());
    }

    private WorkflowRuleApproval create(final Long ruleId, final WorkflowRuleApprovalDto.Request request) {
        final var approval = new WorkflowRuleApproval();

        approval.setRuleId(ruleId);
        approval.setOrder(request.getOrder());
        approval.setApproveType(request.getApproveType());

        return triggerService.create(approval, request.getAssignees());
    }
}
