package com.source.rworkflow.misc.role;

import com.source.rworkflow.misc.user.exception.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository repository;

    public void validateExist(Long id) {
        final var role = repository.findByIdAndDeletedIsFalse(id);

        if (role == null) {
            throw new RoleNotFoundException(id);
        }
    }
}
