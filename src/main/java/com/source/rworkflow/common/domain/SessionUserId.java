package com.source.rworkflow.common.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SessionUserId {
    private Long id;

    public static SessionUserId systemUser(){
        return new SessionUserId(1L);
    }
}
