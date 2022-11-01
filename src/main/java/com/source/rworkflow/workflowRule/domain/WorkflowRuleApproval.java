package com.source.rworkflow.workflowRule.domain;

import com.source.rworkflow.workflowRule.type.ApprovalType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "workflow_rule_approvals")
@Getter
@Setter
@Slf4j
public class WorkflowRuleApproval {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_id", nullable = false)
    private Long ruleId;

    @Column(name = "order", nullable = false)
    private Long order;

    @Column(name = "approve_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalType approvalType;
}
