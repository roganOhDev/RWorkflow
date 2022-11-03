package com.source.rworkflow.workflowRule;

import com.source.rworkflow.misc.user.UserTokenService;
import com.source.rworkflow.workflowRule.domain.rule.WorkflowRuleTransferService;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = "/workflow-rule", produces = {MediaType.APPLICATION_JSON_VALUE})
public class WorkflowRuleController {
    private final UserTokenService userTokenService;
    private final WorkflowRuleTransferService transferService;

    @PutMapping
    public WorkflowRuleDto.Response create(final @Valid @RequestBody WorkflowRuleDto.Create.Request request, @RequestHeader(HEADER_KEY) String userToken) {
        final var sessionUserId = userTokenService.findUserId(userToken);

        return transferService.create(request, sessionUserId);
    }

    @DeleteMapping(path = "/{id}")
    public WorkflowRuleDto.Delete.Response delete(final @PathVariable Long id, @RequestHeader(HEADER_KEY) String userToken) {
        final var sessionUserId = userTokenService.findUserId(userToken);

        return transferService.delete(id, sessionUserId);
    }

    @PatchMapping
    public WorkflowRuleDto.Response update(final @Valid @RequestBody WorkflowRuleDto.Update.Request request, @RequestHeader(HEADER_KEY) String userToken) {
        final var sessionUserId = userTokenService.findUserId(userToken);

        return transferService.update(request, sessionUserId);
    }

    @GetMapping(path = {"list/{workflowRequestType}", "list"})
    public WorkflowRuleDto.Read.Response list(final @PathVariable(required = false) String workflowRequestType) {
        return transferService.list(workflowRequestType);
    }

    @GetMapping(path = "{id}")
    public WorkflowRuleDto.Response find(final @PathVariable Long id, @RequestHeader(HEADER_KEY) String userToken) {
        final var sessionUserId = userTokenService.findUserId(userToken);

        return transferService.find(id);
    }

}
