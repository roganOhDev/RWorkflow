package com.source.rworkflow.misc.user;

import com.source.rworkflow.common.domain.SessionUserId;
import com.source.rworkflow.misc.user.exception.UserTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserTokenService {
    private final UserTokenRepository repository;

    public SessionUserId findUserId(final String token) {
        final var userToken = repository.findByToken(token);

        if (userToken == null) {
            throw new UserTokenNotFoundException(token);
        }

        return new SessionUserId(userToken.getId());
    }
}
