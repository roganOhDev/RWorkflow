package com.source.rworkflow.workflowRule.domain.approvalAssignee;

import com.source.rworkflow.workflowRule.type.AssigneeType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "workflow_rule_approval_assignees")
@Getter
@Setter
@Slf4j
public class WorkflowRuleApprovalAssignee {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_approval_id", nullable = false)
    private Long ruleApprovalId;

    @Column(name = "assignee_type", nullable = false)
    private AssigneeType assigneeType;

    @Column(name = "assignee_value", nullable = false)
    private Long assigneeValue;
}