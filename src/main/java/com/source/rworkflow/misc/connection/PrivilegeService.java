package com.source.rworkflow.misc.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivilegeService {
    private final PrivilegeRepository repository;
}
