package com.source.rworkflow.workflowRule.fail;

import com.source.rworkflow.ClientFactory;
import com.source.rworkflow.Const;
import com.source.rworkflow.TestCase;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import com.source.rworkflow.workflowRule.WorkflowRuleControllerTestCommon;
import com.source.rworkflow.workflowRule.type.ApproveType;
import com.source.rworkflow.workflowRule.type.AssigneeType;
import jdk.jfr.Description;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

class WorkflowRuleControllerTest extends TestCase {
    private final static String CLASS_URL = "/workflow-rule";

    @Description("name, type, urgent 중 null 을 넣으면 fail 해야 한다.")
    @TestFactory
    Collection<DynamicNode> simple_create() {
        return group(
                single("urgent null", () -> {
                    final var body = new JSONObject();

                    body.put("name", "name");
                    body.put("type", WorkflowRequestType.ACCESS_CONTROL.name());

                    ClientFactory.put(CLASS_URL, body, Const.User.Admin.token);
                }),

                single("type null", () -> {
                    final var body = new JSONObject();

                    body.put("name", "name");
                    body.put("urgent", false);

                    ClientFactory.put(CLASS_URL, body, Const.User.Admin.token);
                })
        );
    }

    @DisplayName("urgent 이면서 approval가 있으면 에러가 나야한다.")
    @TestFactory
    Collection<DynamicNode> urgent_approval_error() {
        return group(
                single("테스트", () -> {
                    final var body = new JSONObject();

                    body.put("name", "name");
                    body.put("type", WorkflowRequestType.ACCESS_CONTROL.name());
                    body.put("urgent", true);

                    final var assignee1 = WorkflowRuleControllerTestCommon.create_assignee(AssigneeType.USER, 2L);
                    final var assignee2 = WorkflowRuleControllerTestCommon.create_assignee(AssigneeType.USER, 3L);

                    final var approval = WorkflowRuleControllerTestCommon.create_approval(ApproveType.ALL, 1L, List.of(assignee1, assignee2));

                    body.put("approvals", List.of(approval));

                    System.out.println(body);

                    ClientFactory.put(CLASS_URL, body, Const.User.Admin.token);
                })
        );
    }

}