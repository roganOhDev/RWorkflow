package com.source.rworkflow.workflow.controller;

import com.source.rworkflow.misc.user.token.UserTokenService;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.source.rworkflow.common.domain.UserTokenHeader.HEADER_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/workflow/approve/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
public class WorkflowApproveController {
    private final WorkflowTransferService transferService;
    private final UserTokenService userTokenService;

    @PostMapping(value = "/ok")
    public WorkflowRequestDto.Approve.Response approve(@PathVariable Long id, @RequestParam Long order, @RequestHeader(HEADER_KEY) String userToken) {
        final var sessionUserId = userTokenService.findUserId(userToken);

        return transferService.approve(id, order, sessionUserId, true);
    }

    @PostMapping(value = "/reject")
    public WorkflowRequestDto.Approve.Response disApprove(@PathVariable Long id, @RequestParam Long order, @RequestHeader(HEADER_KEY) String userToken) {
        final var sessionUserId = userTokenService.findUserId(userToken);

        return transferService.approve(id, order, sessionUserId, false);
    }
}
