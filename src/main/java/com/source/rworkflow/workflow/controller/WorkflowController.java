package com.source.rworkflow.workflow.controller;

import com.source.rworkflow.common.exception.RException;
import com.source.rworkflow.misc.user.UserTokenService;
import com.source.rworkflow.misc.user.exception.UserNotFoundException;
import com.source.rworkflow.workflow.dto.WorkflowRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.source.rworkflow.common.domain.UserTokenHeader.HEADER_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/workflow", produces = {MediaType.APPLICATION_JSON_VALUE})
public class WorkflowController {
    private final WorkflowTransferService transferService;
    private final UserTokenService userTokenService;

    @PutMapping
    public WorkflowRequestDto.SaveResponse create(final @Valid @RequestBody WorkflowRequestDto.Create.Request createRequest, @RequestHeader(HEADER_KEY) String userToken) {
        final var sessionUserId = userTokenService.findUserId(userToken);

        return transferService.create(createRequest, sessionUserId);
    }

    @GetMapping(value = "/{id}")
    public void find(@PathVariable Long id) {

    }

    @GetMapping
    public void list() {
        throw new UserNotFoundException(3L);

    }

    @PatchMapping
    public void update() {

    }

    @PostMapping(value = "/{id}")
    public void cancel(@PathVariable Long id) {

    }
}
