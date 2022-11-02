package com.source.rworkflow.workflowRule.domain.approval;

import com.source.rworkflow.workflowRule.dto.AssigneeDto;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleApprovalDto;
import com.source.rworkflow.workflowRule.exception.AssigneeCanNotBeEmpty;
import com.source.rworkflow.workflowRule.exception.OrderValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRuleApprovalCompositeService {
    private final WorkflowRuleApprovalTriggerService triggerService;

    @Transactional
    public List<WorkflowRuleApproval> createCollection(final Long ruleId, final List<WorkflowRuleApprovalDto.Request> requests) {
        return requests.stream()
                .map(request -> create(ruleId, request))
                .collect(Collectors.toUnmodifiableList());
    }

    private WorkflowRuleApproval create(final Long ruleId, final WorkflowRuleApprovalDto.Request request) {
        validate(request);

        final var approval = new WorkflowRuleApproval();

        approval.setRuleId(ruleId);
        approval.setOrder(request.getOrder());
        approval.setApproveType(request.getApproveType());

        return triggerService.create(approval, request.getAssignees());
    }

    private void validate(final WorkflowRuleApprovalDto.Request request) {
        checkOrder(request.getOrder());
        checkAssigneeCount(request.getAssignees());
    }

    private void checkOrder(final Long order) {
        if (order > 3 | order <= 0) {
            throw new OrderValueException();
        }
    }

    private void checkAssigneeCount(final List<AssigneeDto.Request> list) {
        if (list.size() == 0) {
            throw new AssigneeCanNotBeEmpty();
        }

    }
}
