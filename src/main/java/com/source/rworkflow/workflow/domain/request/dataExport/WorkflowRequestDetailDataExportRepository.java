package com.source.rworkflow.workflow.domain.request.dataExport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRequestDetailDataExportRepository extends JpaRepository<WorkflowRequestDetailDataExport, Long> {
}
