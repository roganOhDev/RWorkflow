package com.source.rworkflow.misc.connection;

import com.source.rworkflow.misc.role.exception.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConnectionService {
    private final ConnectionRepository repository;

    public void validateExist(Long id) {
        final var connection = repository.findByIdAndDeletedIsFalse(id);

        if (connection == null) {
            throw new RoleNotFoundException(id);
        }
    }
}
