package com.source.rworkflow.workflow.domain.approval;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflow.dto.WorkflowApprovalDto;
import com.source.rworkflow.workflow.exception.SelfApproveException;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    private WorkflowRequestApproval create(final Long requestId, final WorkflowApprovalDto.Create.Request creatRequest, final SessionUserId sessionUserId) {
        final var workflowRequestApproval = getNewWorkflowRequestApproval(requestId, creatRequest);

        validateSelfApprove(creatRequest.getAssignees(), sessionUserId);

        return triggerService.create(creatRequest.getAssignees(), requestId, workflowRequestApproval);
    }

    private WorkflowRequestApproval create(final Long requestId, final WorkflowApprovalDto.Create.Request creatRequest,
                                           final List<Long> assigneesByRule, final SessionUserId sessionUserId) {
        final var workflowRequestApproval = getNewWorkflowRequestApproval(requestId, creatRequest);

        final var resultList = new ArrayList<>(creatRequest.getAssignees());
        resultList.addAll(assigneesByRule);
        final var assignees = ListUtil.removeDuplicateElement(resultList);

        validateSelfApprove(assignees, sessionUserId);

        return triggerService.create(assignees, requestId, workflowRequestApproval);
    }

    private void validateSelfApprove(final List<Long> assignees, final SessionUserId sessionUserId) {
        if (assignees.contains(sessionUserId.getId())) {
            throw new SelfApproveException(sessionUserId);
        }
    }

    private WorkflowRequestApproval getNewWorkflowRequestApproval(final Long requestId,  final WorkflowApprovalDto.Create.Request creatRequest){
        final var workflowRequestApproval = new WorkflowRequestApproval();

        workflowRequestApproval.setRequestId(requestId);
        workflowRequestApproval.setApproveType(creatRequest.getApproveType());
        workflowRequestApproval.setOrder(creatRequest.getOrder());

        return workflowRequestApproval;
    }
}