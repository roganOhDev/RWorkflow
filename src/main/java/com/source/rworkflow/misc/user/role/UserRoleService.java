package com.source.rworkflow.misc.user.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository repository;

    public List<Long> findUserIdByRole(Long roleId) {
        return repository.findAllByRoleIdAndDeletedIsFalse(roleId).stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toUnmodifiableList());
    }
}
