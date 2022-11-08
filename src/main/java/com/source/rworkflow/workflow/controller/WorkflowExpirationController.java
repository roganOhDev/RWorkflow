package com.source.rworkflow.workflow.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/workflow/expire/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
public class WorkflowExpirationController {
    private final WorkflowTransferService transferService;

    @PostMapping(path = "/execution")
    public void executionExpire(@PathVariable Long id) {
       transferService.executionExpire(id);
    }

    @PostMapping(path = "/request")
    public void requestExpire(@PathVariable Long id) {
        transferService.requestExpire(id);
    }
}
