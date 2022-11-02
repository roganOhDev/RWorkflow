package com.source.rworkflow.workflowRule;

import com.source.rworkflow.workflowRule.type.ApproveType;
import com.source.rworkflow.workflowRule.type.AssigneeType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WorkflowRuleControllerTestCommon {
    public static JSONObject create_assignee(final AssigneeType type, final Long value) throws JSONException {
        final var assignee = new JSONObject();

        assignee.put("type", type.name());
        assignee.put("value", value);

        return assignee;
    }

    public static JSONObject create_approval(final ApproveType type, final Long order, List<JSONObject> assignees) throws JSONException {
        final var approval = new JSONObject();

        final var ja = new JSONArray();
        assignees.forEach(ja::put);

        final var tmpObject = new JSONObject();
        tmpObject.put("list", assignees);

        approval.put("approveType", type.name());
        approval.put("order", order);
        approval.put("assignees", ja);

        return approval;
    }
}
