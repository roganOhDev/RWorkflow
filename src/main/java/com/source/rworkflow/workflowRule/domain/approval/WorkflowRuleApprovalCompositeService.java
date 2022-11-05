package com.source.rworkflow.workflowRule.domain.approval;

import com.source.rworkflow.common.util.Comparer;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleApprovalDto;
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
    private final WorkflowRuleApprovalService service;

    @Transactional
    public List<WorkflowRuleApproval> createCollection(final Long ruleId, final List<WorkflowRuleApprovalDto.Request> requests) {
        return requests.stream()
                .map(request -> create(ruleId, request))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void deleteCollection(final Long ruleId) {
        final var workflowRuleApprovals = findAllByRuleId(ruleId);

        workflowRuleApprovals.forEach(triggerService::delete);
    }

    @Transactional
    public List<WorkflowRuleApproval> updateCollection(final Long ruleId, List<WorkflowRuleApprovalDto.Request> requests) {
        final var approvals = findAllByRuleId(ruleId);

        final var results = Comparer.compare(approvals, requests, (approval, request) -> approval.getId().equals(request.getId()));

        return results.execute(
                createRequest -> this.create(ruleId, createRequest),
                triggerService::delete
        );
    }

    @Transactional(readOnly = true)
    public List<WorkflowRuleApproval> findAllByRuleId(final Long ruleId) {
        return service.findAllByRuleId(ruleId);
    }

    private WorkflowRuleApproval create(final Long ruleId, final WorkflowRuleApprovalDto.Request request) {
        validateCreate(request);

        final var approval = new WorkflowRuleApproval();

        approval.setRuleId(ruleId);
        approval.setOrder(request.getOrder());
        approval.setApproveType(request.getApproveType());

        return triggerService.create(approval, request.getAssignees());
    }


    private void validateCreate(final WorkflowRuleApprovalDto.Request request) {
        checkOrder(request.getOrder());
    }

    private void checkOrder(final Long order) {
        if (order > 3 || order <= 0) {
            throw new OrderValueException();
        }
    }
}
