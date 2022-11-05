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
@RequestMapping(value = "/workflow/approve/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
public class WorkflowApproveController {
    private final WorkflowTransferService transferService;
    private final UserTokenService userTokenService;

    @PostMapping(value = "/ok")
    public void approve(@PathVariable Long id) {

    }

    @PostMapping(value = "/reject")
    public void disApprove(@PathVariable Long id) {

    }
}
