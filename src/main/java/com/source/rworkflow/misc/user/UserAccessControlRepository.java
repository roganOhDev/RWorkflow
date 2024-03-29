package com.source.rworkflow.misc.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccessControlRepository extends JpaRepository<UserAccessControl, Long> {
}
