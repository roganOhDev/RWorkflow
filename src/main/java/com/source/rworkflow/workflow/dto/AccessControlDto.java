package com.source.rworkflow.workflow.dto;

import lombok.Getter;

import javax.validation.Valid;
import java.util.List;

public class AccessControlDto {
    @Getter
    public static class Request {
        @Valid
        private List<AccessControlConnectionDto.Request> connections;
    }
}
