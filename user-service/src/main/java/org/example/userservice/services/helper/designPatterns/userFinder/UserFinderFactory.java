package org.example.userservice.services.helper.designPatterns.userFinder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
@RequiredArgsConstructor
public class UserFinderFactory {
    private final Map<String, UserFinderStrategy> strategies;

    public UserFinderStrategy getStrategy(String role) {
        UserFinderStrategy strategy = strategies.get(role.toUpperCase());
        //throw new IllegalArgumentException("Unknown role: " + role);
        return strategy;
    }
}
