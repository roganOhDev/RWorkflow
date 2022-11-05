package com.source.rworkflow.workflow.domain.request.dataExport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRequestDetailDataExportRepository extends JpaRepository<WorkflowRequestDetailDataExport, Long> {
    List<WorkflowRequestDetailDataExport> findAllByRequestId(Long requestId);
}
