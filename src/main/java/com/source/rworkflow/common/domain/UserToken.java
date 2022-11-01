package com.source.rworkflow.common.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@EqualsAndHashCode(of = "token")
@Getter
public class UserToken {
    private String token;

    public UserToken(String token) {
        this.token = token;
    }

    public UserToken() {
    }

    public Long userId() {
        if (this.token.equals("a")) {
            return 2L;
        }
        return 1L;
    }
}
