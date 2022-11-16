package com.source.rworkflow.workflow.domain.executeAssignee;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.common.util.ListUtil;
import com.source.rworkflow.workflow.domain.request.WorkflowRequest;
import com.source.rworkflow.workflow.exception.AssigneeCanNotAction;
import com.source.rworkflow.workflow.exception.AssigneeCanNotBeEmpty;
import com.source.rworkflow.workflow.exception.ExecutionAssigneeNotFoundException;
import com.source.rworkflow.workflow.type.ActionType;
import com.source.rworkflow.workflow.type.AssigneeStatusType;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import com.source.rworkflow.workflowRule.domain.WorkflowRuleSuite;
import com.source.rworkflow.workflowRule.domain.executionAssignee.WorkflowRuleExecutionAssignee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowRequestExecutionAssigneeCompositeService {
    private final WorkflowRequestExecutionAssigneeService service;

    private final static String REQUEST_EXECUTION_ASSIGNEE = "requestAssignee";
    private final static String OTHER_EXECUTION_ASSIGNEES = "otherAssignees";

    @Transactional
    public List<WorkflowRequestExecutionAssignee> createCollection(final WorkflowRequest request, final List<Long> executeAssignees,
                                                                   final WorkflowRuleSuite workflowRuleSuite) {
        var assignees = ListUtil.removeDuplicateElement(executeAssignees);
        if (workflowRuleSuite != null) {
            assignees = mergeAssignees(workflowRuleSuite.getExecutionAssignees(), executeAssignees);
        }

        validateAssigneeCount((long) assignees.size(), request.getType());

        return assignees.stream()
                .map(assignee -> create(request.getId(), assignee, request.isUrgent()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void execute(final Long requestId, final SessionUserId sessionUserId) {
        final HashMap<String, List<WorkflowRequestExecutionAssignee>> executionAssigneesMap = getAssigneeMapOfIsCurrentAssignee(requestId, sessionUserId.getId());

        final var currentAssignee = getCurrentAssignee(executionAssigneesMap, ActionType.EXECUTION_END);
        final var otherAssignees = executionAssigneesMap.get(OTHER_EXECUTION_ASSIGNEES);

        currentAssignee.setStatus(AssigneeStatusType.IN_PROGRESS);
        service.updateStatus(currentAssignee);

        otherAssignees.forEach(otherAssignee -> {
            otherAssignee.setStatus(AssigneeStatusType.AUTO_CONFIRMED);
            service.updateStatus(otherAssignee);
        });
    }

    @Transactional
    public void executeSuccess(final Long requestId, final Long executeUserId) {
        final HashMap<String, List<WorkflowRequestExecutionAssignee>> executionAssigneesMap = getAssigneeMapOfIsCurrentAssignee(requestId, executeUserId);

        final var currentAssignee = getCurrentAssignee(executionAssigneesMap, ActionType.EXECUTION_END);
        final var otherAssignees = executionAssigneesMap.get(OTHER_EXECUTION_ASSIGNEES);

        currentAssignee.setStatus(AssigneeStatusType.SUCCEEDED);
        service.updateStatus(currentAssignee);

        otherAssignees.forEach(otherAssignee -> {
            otherAssignee.setStatus(AssigneeStatusType.AUTO_CONFIRMED);
            service.updateStatus(otherAssignee);
        });
    }

    @Transactional
    public void executeFail(final Long requestId, final Long executeUserId) {
        final WorkflowRequestExecutionAssignee currentAssignee = getCurrentAssignee(requestId, executeUserId);

        currentAssignee.setStatus(AssigneeStatusType.FAILED);

        service.updateStatus(currentAssignee);
    }

    private static WorkflowRequestExecutionAssignee getCurrentAssignee(HashMap<String, List<WorkflowRequestExecutionAssignee>> executionAssigneesMap, final ActionType actionType) {
        final var requestAssignee = executionAssigneesMap.get(REQUEST_EXECUTION_ASSIGNEE).get(0);

        if (!requestAssignee.getStatus().equals(AssigneeStatusType.IN_PROGRESS)) {
            throw new AssigneeCanNotAction(actionType.name(), requestAssignee.getStatus());
        }
        return requestAssignee;
    }

    private WorkflowRequestExecutionAssignee getCurrentAssignee(Long requestId, Long executeUserId) {
        final HashMap<String, List<WorkflowRequestExecutionAssignee>> executionAssigneesMap = getAssigneeMapOfIsCurrentAssignee(requestId, executeUserId);

        return getCurrentAssignee(executionAssigneesMap, ActionType.EXECUTION_END);
    }

    private HashMap<String, List<WorkflowRequestExecutionAssignee>> getAssigneeMapOfIsCurrentAssignee(Long requestId, Long executeUserId) {
        final var executionAssignees = service.findByRequestId(requestId);

        final var now = LocalDateTime.now();

        executionAssignees.forEach(assignee -> {
            assignee.setActionBy(executeUserId);
            assignee.setActionAt(now);
        });

        final var executionAssigneesMap = convertToAssigneesMapByIsExecutionRequested(executionAssignees, executeUserId);
        return executionAssigneesMap;
    }


    @Transactional
    public void setAssigneesPending(final Long requestId, final SessionUserId sessionUserId) {
        final var executionAssignees = service.findByRequestId(requestId);

        final var now = LocalDateTime.now();

        executionAssignees.forEach(assignee -> {
                    assignee.setStatus(AssigneeStatusType.PENDING);
                    assignee.setActionAt(now);
                    assignee.setActionBy(sessionUserId.getId());

                    service.updateStatus(assignee);
                }
        );
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequestExecutionAssignee> findAll() {
        return service.findAll();
    }

    @Transactional(readOnly = true)
    public List<WorkflowRequestExecutionAssignee> findByRequestId(final Long requestId) {
        return service.findByRequestId(requestId);
    }


    private HashMap<String, List<WorkflowRequestExecutionAssignee>> convertToAssigneesMapByIsExecutionRequested(List<WorkflowRequestExecutionAssignee> executionAssignees, final Long sessionUserId) {
        Map<Boolean, List<WorkflowRequestExecutionAssignee>> executionAssigneesMap = executionAssignees.stream()
                .collect(Collectors.groupingBy(assignee -> assignee.getAssigneeId().equals(sessionUserId), Collectors.toUnmodifiableList()));

        if (executionAssigneesMap.get(true).isEmpty()) {
            throw new ExecutionAssigneeNotFoundException(sessionUserId);
        }

        final var requestAssignee = executionAssigneesMap.get(true).get(0);

        List<WorkflowRequestExecutionAssignee> otherAssignees = executionAssigneesMap.get(false);
        if (otherAssignees == null) {
            otherAssignees = List.of();
        }

        final var response = new HashMap<String, List<WorkflowRequestExecutionAssignee>>();

        response.put(REQUEST_EXECUTION_ASSIGNEE, List.of(requestAssignee));
        response.put(OTHER_EXECUTION_ASSIGNEES, otherAssignees);

        return response;
    }

    private void validateAssigneeCount(final Long size, final WorkflowRequestType type) {
        if (!type.equals(WorkflowRequestType.ACCESS_CONTROL) && size == 0) {
            throw new AssigneeCanNotBeEmpty("Execution");
        }
    }

    private WorkflowRequestExecutionAssignee create(final Long requestId, final Long createRequest, final boolean urgent) {
        final var assignee = new WorkflowRequestExecutionAssignee();

        assignee.setRequestId(requestId);
        assignee.setAssigneeId(createRequest);

        if (urgent) {
            return service.createUrgent(assignee);
        }
        return service.create(assignee);
    }

    private List<Long> mergeAssignees(final List<WorkflowRuleExecutionAssignee> ruleExecutionAssignees, List<Long> executionAssignees) {
        final var ruleAssignees = new ArrayList<>(ruleExecutionAssignees.stream()
                .map(WorkflowRuleExecutionAssignee::getAssigneeValue)
                .collect(Collectors.toUnmodifiableList()));

        ruleAssignees.addAll(executionAssignees);

        return ListUtil.removeDuplicateElement(ruleAssignees);
    }
}
