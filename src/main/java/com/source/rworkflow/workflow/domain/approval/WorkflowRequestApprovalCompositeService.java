package com.source.rworkflow.workflow.domain.approval;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflow.dto.WorkflowApprovalDto;
import com.source.rworkflow.workflow.exception.AssigneeCanNotBeEmpty;
import com.source.rworkflow.workflow.exception.SelfApproveException;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRequestApprovalCompositeService {
    private final WorkflowRequestApprovalTriggerService triggerService;
    private final WorkflowRequestApprovalService service;

    @Transactional
    public List<WorkflowRequestApproval> createCollection(final Long requestId, final List<WorkflowApprovalDto.Create.Request> createRequests,
                                                          final WorkflowRuleSuite workflowRuleSuite, final SessionUserId sessionUserId) {
        final var resultList = createRequests.stream()
                .map(createRequest -> workflowRuleSuite == null
                        ? create(requestId, createRequest, sessionUserId)
                        : create(requestId, createRequest, workflowRuleSuite.findAllApprovalAssigneeByOrder(createRequest.getOrder()), sessionUserId))
                .collect(Collectors.toUnmodifiableList());

        return ListUtil.removeDuplicateElement(resultList);
    }

    @Transactional(readOnly = true)
    public Map<Long, List<WorkflowRequestApproval>> findAll() {
        return service.findAll().stream()
                .collect(Collectors.groupingBy(WorkflowRequestApproval::getRequestId));
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequestApproval> findByRequestId(final Long requestId){
        return service.findByRequestId(requestId);
    }

    private WorkflowRequestApproval create(final Long requestId, final WorkflowApprovalDto.Create.Request creatRequest, final SessionUserId sessionUserId) {
        final var workflowRequestApproval = getNewWorkflowRequestApproval(requestId, creatRequest);

        validateSelfApprove(creatRequest.getAssignees(), sessionUserId);
        validateAssigneeCount((long) creatRequest.getAssignees().size());

        return triggerService.create(creatRequest.getAssignees(), requestId, workflowRequestApproval);
    }

    private WorkflowRequestApproval create(final Long requestId, final WorkflowApprovalDto.Create.Request creatRequest,
                                           final List<Long> assigneesByRule, final SessionUserId sessionUserId) {
        final var workflowRequestApproval = getNewWorkflowRequestApproval(requestId, creatRequest);

        validateSelfApprove(creatRequest.getAssignees(), sessionUserId);

        final var assignees = mergeAssignees(creatRequest, assigneesByRule, sessionUserId);
        validateAssigneeCount((long) assignees.size());

        return triggerService.create(assignees, requestId, workflowRequestApproval);
    }

    private void validateAssigneeCount(final Long size) {
        if (size == 0) {
            throw new AssigneeCanNotBeEmpty("Execution");
        }
    }

    private void validateSelfApprove(final List<Long> assignees, final SessionUserId sessionUserId) {
        if (assignees.contains(sessionUserId.getId())) {
            throw new SelfApproveException(sessionUserId);
        }
    }

    private List<Long> mergeAssignees(WorkflowApprovalDto.Create.Request creatRequest, List<Long> assigneesByRule, SessionUserId sessionUserId) {
        final var ruleAssignees = removeSelfFromAssigneesByRule(assigneesByRule, sessionUserId);

        final var resultList = new ArrayList<>(creatRequest.getAssignees());
        resultList.addAll(ruleAssignees);
        return ListUtil.removeDuplicateElement(resultList);
    }

    private List<Long> removeSelfFromAssigneesByRule(List<Long> assigneesByRule, SessionUserId sessionUserId) {
        final var mutableAssigneesByRule = new ArrayList<>(assigneesByRule);
        mutableAssigneesByRule.remove(sessionUserId.getId());

        return mutableAssigneesByRule;
    }

    private WorkflowRequestApproval getNewWorkflowRequestApproval(final Long requestId,  final WorkflowApprovalDto.Create.Request creatRequest){
        final var workflowRequestApproval = new WorkflowRequestApproval();

        workflowRequestApproval.setRequestId(requestId);
        workflowRequestApproval.setApproveType(creatRequest.getApproveType());
        workflowRequestApproval.setOrder(creatRequest.getOrder());

        return workflowRequestApproval;
    }
}
