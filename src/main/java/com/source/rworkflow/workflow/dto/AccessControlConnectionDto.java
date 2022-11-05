package com.source.rworkflow.workflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.source.rworkflow.common.util.DateFormat;
import com.source.rworkflow.workflow.domain.request.accessControl.connection.WorkflowRequestDetailAccessControlConnection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormat.FORMAT, timezone = "Asia/Seoul")
        private LocalDateTime expirationDate;

        public static Response from(final WorkflowRequestDetailAccessControlConnection entity, final LocalDateTime expirationDate) {
            return new Response(entity.getConnectionId(), entity.getPrivilegeId(), entity.isGranted(), expirationDate);
        }
    }
}
