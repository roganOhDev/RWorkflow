package com.source.rworkflow.workflow.domain;

import com.source.rworkflow.workflow.type.AssigneeStatusType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@Slf4j
@Getter
@Setter
@MappedSuperclass
public class Assignee {
    @Column(name = "assignee_id", nullable = false)
    private Long assigneeId;

    @Column(name = "request_id", nullable = false)
    private Long requestId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssigneeStatusType status;
}
