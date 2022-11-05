package com.source.rworkflow.workflow.dto;

import com.source.rworkflow.workflow.domain.request.dataExport.WorkflowRequestDetailDataExport;
import com.source.rworkflow.workflow.type.DataContentType;
import com.source.rworkflow.workflow.type.SqlContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class DataExportDto {
    @Getter
    public static class Request {
        @NotNull(message = "Must Have Connection Id")
        private Long connectionId;
        private String database;
        @NotNull(message = "Must Have ContentType")
        private DataContentType contentType;
        @NotNull(message = "Must Have contentValue")
        private String contentValue;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long connectionId;
        private String database;
        private DataContentType contentType;
        private String contentValue;

        public static Response from(final WorkflowRequestDetailDataExport entity) {
            return new Response(entity.getConnectionId(), entity.getDatabase(), entity.getContentType(), entity.getContentValue());
        }
    }
}
