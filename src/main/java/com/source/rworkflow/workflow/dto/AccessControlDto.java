package com.source.rworkflow.workflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.source.rworkflow.common.util.DateFormat;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class AccessControlDto {
    @Getter
    public static class Request {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormat.FORMAT, timezone = "Asia/Seoul")
        private LocalDateTime expirationDate;
        @Valid
        @Size(min = 1)
        private List<AccessControlConnectionDto.Request> connections;
    }
}
