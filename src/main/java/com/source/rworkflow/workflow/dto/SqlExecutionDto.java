package com.source.rworkflow.workflow.dto;

import com.source.rworkflow.workflow.type.SqlContentType;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class SqlExecutionDto {
    @Getter
    public static class Request {
        private String database;
        @NotNull(message = "Must Have ContentType")
        private SqlContentType contentType;
        @NotNull(message = "Must Have contentValue")
        private String contentValue;
        private LocalDateTime executionExpiryAt;
        private LocalDateTime expiryAt;
    }
}
