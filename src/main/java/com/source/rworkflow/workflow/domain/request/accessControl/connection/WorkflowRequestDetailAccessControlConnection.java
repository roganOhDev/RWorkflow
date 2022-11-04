package com.source.rworkflow.workflow.domain.request.accessControl.connection;

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
@Table(name = "workflow_request_detail_access_control_connections")
@Getter
@Setter
@Slf4j
public class WorkflowRequestDetailAccessControlConnection {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "detail_access_control_id", nullable = false)
    private Long detailAccessControlId;

    @Column(name = "connection_id", nullable = false)
    private Long connectionId;

    @Column(name = "privilege_id", nullable = false)
    private Long privilegeId;

    @Column(name = "granted", nullable = false)
    private boolean granted;
}
