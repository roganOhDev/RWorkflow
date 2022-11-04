package com.source.rworkflow.common.domain;

import lombok.Getter;

@Getter
public class ExceptionDto {
    private final String path;
    private final int status;
    private final String message;
    private String exception;

    public ExceptionDto(final Exception e) {
        this.path = e.getStackTrace()[0].toString();
        this.status = 400;
        this.message = e.getMessage();
        this.exception = e.getClass().getSimpleName();
    }
}
