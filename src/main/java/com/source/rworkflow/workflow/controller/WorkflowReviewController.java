package com.source.rworkflow.workflow.controller;

import com.source.rworkflow.misc.user.token.UserTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.source.rworkflow.common.domain.UserTokenHeader.HEADER_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/workflow/review/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
public class WorkflowReviewController {
    private final WorkflowTransferService transferService;
    private final UserTokenService userTokenService;

    @PostMapping
    public void review(@PathVariable Long id, @RequestParam(HEADER_KEY) String userToken) {
        final var sessionUserId = userTokenService.findUserId(userToken);

        transferService.review(id, sessionUserId);
    }
}
