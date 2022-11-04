package com.source.rworkflow.workflow.dto;

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
}
