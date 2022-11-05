package com.source.rworkflow.workflow.controller;

import com.source.rworkflow.misc.user.token.UserTokenService;
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
@RequestMapping(value = "/workflow/execute/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
public class WorkflowExecutionController {
    private final WorkflowTransferService transferService;
    private final UserTokenService userTokenService;

    @PostMapping
    public void execute(@PathVariable Long id){

    }

    @PostMapping(value = "/success")
    public void executeSuccess(@PathVariable Long id) {

    }

    @PostMapping(value = "/fail")
    public void executeFail(@PathVariable Long id) {
    }
}
