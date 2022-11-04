package com.source.rworkflow.misc.user;

import com.source.rworkflow.misc.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public void validateExist(Long id) {
        find(id);
    }

    public User find(Long id) {
        final var user = repository.findByIdAndDeletedIsFalse(id);

        if(user==null) {
            throw new UserNotFoundException(id);
        }

        return user
    }
}
