package org.example.userservice.services.helper.roleIDFinder;

import lombok.RequiredArgsConstructor;
import org.example.userservice.services.helper.userSaving.UserSave;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
@RequiredArgsConstructor
public class RoleFinderFactory {
    private final Map<String, RoleFinderStrategy> strategies;

    public RoleFinderStrategy getStrategy(String role) {
        RoleFinderStrategy strategy = strategies.get(role.toUpperCase());
        if (strategy == null) throw new IllegalArgumentException("Unknown role: " + role);
        return strategy;
    }
}
