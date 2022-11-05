package com.source.rworkflow.workflow.dto;

import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

public class AccessControlDto {
    @Getter
    public static class Request {
        @Valid
        @Size(min = 1)
        private List<AccessControlConnectionDto.Request> connections;
    }
}
