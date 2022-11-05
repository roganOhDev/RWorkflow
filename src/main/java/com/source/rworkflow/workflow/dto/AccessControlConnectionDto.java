package com.source.rworkflow.workflow.dto;

import com.source.rworkflow.workflow.domain.request.accessControl.connection.WorkflowRequestDetailAccessControlConnection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public class AccessControlConnectionDto {
    @Getter
    public static class Request {
        @NotNull(message = "Must Have ConnectionId")
        private Long connectionId;
        @NotNull(message = "Must Have PrivilegeId")
        private Long privilegeId;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long connectionId;
        private Long privilegeId;
        private boolean granted;

        public static Response from(final WorkflowRequestDetailAccessControlConnection entity) {
            return new Response(entity.getConnectionId(), entity.getPrivilegeId(), entity.isGranted());
        }
    }
}
