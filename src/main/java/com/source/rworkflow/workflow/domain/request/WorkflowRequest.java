package com.source.rworkflow.workflow.domain.request;

import com.source.rworkflow.workflow.type.ApprovalStatusType;
import com.source.rworkflow.workflow.type.ExecutionStatusType;
import com.source.rworkflow.workflow.type.ReviewStatusType;
import com.source.rworkflow.workflow.type.SqlContentType;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
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
import java.time.LocalDateTime;

@Entity
@Table(name = "workflow_requests")
@Getter
@Setter
@Slf4j
public class WorkflowRequest {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkflowRequestType type;

    @Column(name = "rule_id", nullable = false)
    private Long ruleId;

    @Column(name = "urgent", nullable = false)
    private boolean urgent;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "approval_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApprovalStatusType approvalStatus;

    @Column(name = "execution_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExecutionStatusType executionStatus;

    @Column(name = "review_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewStatusType reviewStatus;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "requested_by", nullable = false)
    private Long requestedBy;

    @Column(name = "expired", nullable = false)
    private boolean expired;

    @Column(name = "canceled", nullable = false)
    private boolean canceled;

    @Column(name = "canceled_at")
    private LocalDateTime canceldAt;

    @Column(name = "canceled_by")
    private Long canceledBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;
}
