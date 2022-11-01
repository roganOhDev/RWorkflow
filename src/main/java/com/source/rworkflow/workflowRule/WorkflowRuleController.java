package com.source.rworkflow.workflowRule;

import com.source.rworkflow.common.domain.UserToken;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/workflow-rule", produces = {MediaType.APPLICATION_JSON_VALUE})
public class WorkflowRuleController {
    @PutMapping
    private WorkflowRuleDto.Create.Response create(@RequestBody final WorkflowRuleDto.Create.Request request, @AuthenticationPrincipal UserToken userToken) {
        System.out.println(userToken.userId());

        return new WorkflowRuleDto.Create.Response();
    }


}
