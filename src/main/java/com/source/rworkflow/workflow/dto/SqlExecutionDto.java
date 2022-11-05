package com.source.rworkflow.workflow.dto;

import com.source.rworkflow.workflow.domain.request.sqlExecution.WorkflowRequestDetailSqlExecution;
import com.source.rworkflow.workflow.type.SqlContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class SqlExecutionDto {
    @Getter
    public static class Request {
        @NotNull(message = "Must Have ConnectionId")
        private Long connectionId;
        private String database;
        @NotNull(message = "Must Have ContentType")
        private SqlContentType contentType;
        @NotNull(message = "Must Have contentValue")
        private String contentValue;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long connectionId;
        private String database;
        private SqlContentType contentType;
        private String contentValue;

        public static Response from(final WorkflowRequestDetailSqlExecution entity) {
            return new Response(entity.getConnectionId(), entity.getDatabase(), entity.getContentType(), entity.getContentValue());
        }
    }
}
