package com.source.rworkflow.misc.connection;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "connections")
@Getter
@Setter
@Slf4j
public class Connection {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "database_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DatabaseType databaseType;

    @Column(name = "host", nullable = false)
    private String host;

    @Column(name = "port", nullable = false)
    private Long port;

    @Column(name = "username", nullable = false)
    private Long username;

    @Column(name = "password", nullable = false)
    private Long password;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;
}
