package com.source.rworkflow.workflowRule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/workflow-rule")
public class WorkflowRuleController {
    @GetMapping
    public void a() {
        System.out.println("asdf");
    }

}
