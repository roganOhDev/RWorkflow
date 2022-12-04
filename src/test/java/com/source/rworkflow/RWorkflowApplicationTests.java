package com.source.rworkflow;

import com.source.rworkflow.workflow.domain.request.WorkflowRequest;
import com.source.rworkflow.workflow.type.WorkflowRequestType;
import com.source.rworkflow.workflowRule.dto.WorkflowRuleDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.source.rworkflow.common.domain.UserTokenHeader.HEADER_KEY;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RWorkflowApplicationTests extends BaseTest {

    @Test
    public void test() throws Exception {
        final var result = mvc.perform(MockMvcRequestBuilders.get("/workflow/test")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        final var c = objectMapper.readValue(result, WorkflowRequest[].class);
        System.out.println(c);
    }


    @Test
    public void createRule() throws Exception {
        final var request = WorkflowRuleDto.Create.Request.builder()
                .name("name")
                .type(WorkflowRequestType.DATA_EXPORT)
                .urgent(true)
                .approvals(List.of())
                .executionAssignees(List.of())
                .reviewAssignees(List.of())
                .build();

        final var content = objectMapper.writeValueAsString(request);

        final var result = mvc.perform(MockMvcRequestBuilders.put("/workflow-rule")
                        .header(HEADER_KEY, "yCPDeOmN")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        final var a = objectMapper.readValue(result, WorkflowRuleDto.Response.class);
    }
}
