package com.source.rworkflow.workflow.controller;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowTransferService {

    public WorkflowRequestDto.SaveResponse create(final WorkflowRequestDto.Create.Request createRequest, final SessionUserId sessionUserId) {
        return new WorkflowRequestDto.SaveResponse();
    }
}
