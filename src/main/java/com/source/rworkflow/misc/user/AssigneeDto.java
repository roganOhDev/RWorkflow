package com.source.rworkflow.misc.user;

import com.source.rworkflow.workflow.type.AssigneeStatusType;
import lombok.Getter;

@Getter
public class AssigneeDto {
    private Long id;
    private String name;
    private AssigneeStatusType status;

    public static AssigneeDto of(final User user) {
        final var dto = new AssigneeDto();

        dto.id = user.getId();
        dto.name = user.getUsername();

        return dto;
    }
}
