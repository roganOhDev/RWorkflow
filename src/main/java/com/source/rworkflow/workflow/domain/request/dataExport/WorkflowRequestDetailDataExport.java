package com.source.rworkflow.workflow.domain.request.dataExport;

import com.source.rworkflow.workflow.type.DataContentType;
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
@Table(name = "workflow_request_detail_data_exports")
@Getter
@Setter
@Slf4j
public class WorkflowRequestDetailDataExport {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_id", nullable = false)
    private Long requestId;

    @Column(name = "connection_id", nullable = false)
    private Long connectionId;

    @Column(name = "database", nullable = false)
    private String database;

    @Column(name = "content_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataContentType contentType;

    @Column(name = "content_value", nullable = false)
    private String contentValue;

    @Column(name = "execution_expiry_at", nullable = false)
    private LocalDateTime executionExpiryAt;

    @Column(name = "execution_request_at", nullable = false)
    private LocalDateTime request_expiry_at;
}
