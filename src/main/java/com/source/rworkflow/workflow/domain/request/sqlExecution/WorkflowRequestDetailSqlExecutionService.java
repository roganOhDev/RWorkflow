package com.source.rworkflow.workflow.domain.request.sqlExecution;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowRequestDetailSqlExecutionService {
    private final WorkflowRequestDetailSqlExecutionRepository repository;

    public WorkflowRequestDetailSqlExecution create(final WorkflowRequestDetailSqlExecution request){
        return repository.save(request);
    }
}
