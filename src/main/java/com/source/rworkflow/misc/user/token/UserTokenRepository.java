package com.source.rworkflow.misc.user.token;

import com.source.rworkflow.misc.user.token.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    UserToken findByToken(final String token);
}
