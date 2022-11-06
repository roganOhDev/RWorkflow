package com.source.rworkflow.workflow.dto;

import com.source.rworkflow.misc.user.User;
import com.source.rworkflow.workflow.type.AssigneeStatusType;
import com.source.rworkflow.workflowRule.type.AssigneeType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public class AssigneeDto {
    @Getter
    public static class Response {
        private Long id;
        private String name;
        private AssigneeStatusType status;

        public static Response of(final User user, final AssigneeStatusType status) {
            final var dto = new Response();

            dto.id = user.getId();
            dto.name = user.getUsername();
            dto.status = status;

            return dto;
        }
    }

    @Getter
    @EqualsAndHashCode
    public static class Request{
        @NotNull
        private Long id;
        @NotNull
        private AssigneeType type;
    }
}
