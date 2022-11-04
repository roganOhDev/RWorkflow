package com.source.rworkflow.workflow.domain.executeAssignee;

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
@Table(name = "workflow_request_execution_assignees")
@Getter
@Setter
@Slf4j
public class WorkflowRequestExecutionAssignee {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_Id", nullable = false)
    private Long requestId;

    @Column(name = "assignee_id", nullable = false)
    private Long assigneeId;

    @Column(name = "action_at", nullable = false)
    private LocalDateTime actionAt;

    @Column(name = "action_by", nullable = false)
    private Long actionBy;

    @Column(name = "finished_at", nullable = false)
    private LocalDateTime finishedAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssigneeStatusType status;
}
