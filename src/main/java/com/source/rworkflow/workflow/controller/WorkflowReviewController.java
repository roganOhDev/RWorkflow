package com.source.rworkflow.workflow.controller;

import com.source.rworkflow.misc.user.UserTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/workflow", produces = {MediaType.APPLICATION_JSON_VALUE})
public class WorkflowReviewController {
    private final WorkflowTransferService transferService;
    private final UserTokenService userTokenService;

    @PostMapping(value = "/{id}/review")
    public void review(@PathVariable Long id) {

    }
}
