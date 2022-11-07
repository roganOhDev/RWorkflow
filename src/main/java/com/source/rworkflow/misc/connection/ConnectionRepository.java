package com.source.rworkflow.misc.connection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long>{
    Connection findByIdAndDeletedIsFalse(Long id);

}
