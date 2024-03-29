package com.source.rworkflow.misc.user;

import com.source.rworkflow.misc.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByIdAndDeletedIsFalse(final Long id);
}
