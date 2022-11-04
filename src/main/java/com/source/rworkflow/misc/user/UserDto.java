package com.source.rworkflow.misc.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserDto {
    private Long id;
    private String name;

    public static UserDto of(final User user) {
        final var dto = new UserDto();

        dto.id = user.getId();
        dto.name = user.getUsername();

        return dto;
    }
}
