package com.source.rworkflow.workflowRule;

import com.source.rworkflow.ClientFactory;
import com.source.rworkflow.Const;
import com.source.rworkflow.TestCase;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import jdk.jfr.Description;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collection;

class WorkflowRuleControllerTest extends TestCase {
    private final static String CLASS_URL = "/workflow-rule";

    @Description("유저 토큰 테스트")
    @TestFactory
    Collection<DynamicNode> simple_create() {
        return group(
                single("simple create", () -> {
                    final var body = new JSONObject();

                    body.put("name", "name");
                    body.put("type", WorkflowRequestType.ACCESS_CONTROL.name());
                    body.put("urgent", false);

                    final var actual = ClientFactory.put(CLASS_URL, body, Const.User.Admin.token);

                    assertSimpleRule(body, actual, "rule");
                }),

                single("simple null check", () -> {
                    final var body = new JSONObject();

                    body.put("name", "name");
                    body.put("type", WorkflowRequestType.ACCESS_CONTROL.name());

                    final var actual = ClientFactory.put(CLASS_URL, body, Const.User.Admin.token);

                    assertSimpleRule(body, actual, "rule");
                })
        );
    }

    private void assertSimpleRule(final JSONObject expected, final JSONObject actual, final String message) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.get("name"), actual.get("name"), message + "name"),
                () -> Assertions.assertEquals(expected.get("type"), actual.get("type"), message + "type"),
                () -> Assertions.assertEquals(expected.get("urgent"), actual.get("urgent"), message + "urgent")
        );
    }

}