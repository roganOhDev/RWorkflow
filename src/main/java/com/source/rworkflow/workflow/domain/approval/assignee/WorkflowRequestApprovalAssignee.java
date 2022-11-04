package com.source.rworkflow.workflow.domain.approval.assignee;

import com.source.rworkflow.workflow.domain.Assignee;
import com.source.rworkflow.workflow.type.AssigneeStatusType;
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
@Table(name = "workflow_request_approval_assignees")
@Getter
@Setter
@Slf4j
public class WorkflowRequestApprovalAssignee extends Assignee {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_id", nullable = false)
    private Long requestId;

    @Column(name = "request_approval_id", nullable = false)
    private Long requestApprovalId;

    @Column(name = "action_at")
    private LocalDateTime actionAt;

    @Column(name = "action_by")
    private Long actionBy;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssigneeStatusType status;
}
