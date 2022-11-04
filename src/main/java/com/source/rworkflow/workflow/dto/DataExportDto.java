package com.source.rworkflow.workflow.dto;

import com.source.rworkflow.workflow.type.DataContentType;
import com.source.rworkflow.workflow.type.SqlContentType;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class DataExportDto {
    @Getter
    public static class Request {
        @NotNull(message = "Must Have Connection Id")
        private String connectionId;
        private String database;
        @NotNull(message = "Must Have ContentType")
        private DataContentType contentType;
        @NotNull(message = "Must Have contentValue")
        private String contentValue;
        private LocalDateTime executionExpiryAt;
        private LocalDateTime expiryAt;
    }
}
