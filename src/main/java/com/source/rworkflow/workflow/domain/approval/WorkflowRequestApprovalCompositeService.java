package com.source.rworkflow.workflow.domain.approval;

import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflow.dto.WorkflowApprovalDto;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRequestApprovalCompositeService {
    private final WorkflowRequestApprovalTriggerService triggerService;
    private final WorkflowRequestApprovalService service;

    public List<WorkflowRequestApproval> createCollection(final Long requestId, final List<WorkflowApprovalDto.Create.Request> createRequests, final WorkflowRuleSuite workflowRuleSuite) {
        final var resultList = createRequests.stream()
                .map(createRequest -> workflowRuleSuite == null
                        ? create(requestId, createRequest)
                        : create(requestId, createRequest, workflowRuleSuite.findAllApprovalAssigneeByOrder(createRequest.getOrder())))
                .collect(Collectors.toUnmodifiableList());

        return ListUtil.removeDuplicateElement(resultList);
    }

    private WorkflowRequestApproval create(final Long requestId, final WorkflowApprovalDto.Create.Request creatRequest) {
        final var workflowRequestApproval = getNewWorkflowRequestApproval(requestId, creatRequest);

        return triggerService.create(creatRequest.getAssignees(), requestId, workflowRequestApproval);
    }

    private WorkflowRequestApproval create(final Long requestId, final WorkflowApprovalDto.Create.Request creatRequest, final List<Long> assigneesByRule) {
        final var workflowRequestApproval = getNewWorkflowRequestApproval(requestId, creatRequest);

        final var resultList = new ArrayList<>(creatRequest.getAssignees());
        resultList.addAll(assigneesByRule);
        final var assignees = ListUtil.removeDuplicateElement(resultList);

        return triggerService.create(assignees, requestId, workflowRequestApproval);
    }

    private WorkflowRequestApproval getNewWorkflowRequestApproval(final Long requestId,  final WorkflowApprovalDto.Create.Request creatRequest){
        final var workflowRequestApproval = new WorkflowRequestApproval();

        workflowRequestApproval.setRequestId(requestId);
        workflowRequestApproval.setApproveType(creatRequest.getApproveType());
        workflowRequestApproval.setOrder(creatRequest.getOrder());

        return workflowRequestApproval;
    }
}
