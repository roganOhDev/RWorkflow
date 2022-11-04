package com.source.rworkflow.workflow.domain.approval;

import com.source.rworkflow.workflow.type.ApprovalStatusType;
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
@Table(name = "workflow_request_approvals")
@Getter
@Setter
@Slf4j
public class WorkflowRequestApproval {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_Id", nullable = false)
    private Long request_id;

    @Column(name = "order", nullable = false)
    private boolean order;

    @Column(name = "approve_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatusType approveType;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatusType status;
}
