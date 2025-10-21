package org.example.userservice.services.helper.userSaving;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
@RequiredArgsConstructor
public class SignUpFactory {
    private final Map<String, UserSave> strategies;

    public UserSave getStrategy(String role) {
        UserSave strategy = strategies.get(role.toUpperCase());
        if (strategy == null) throw new IllegalArgumentException("Unknown role: " + role);
        return strategy;
    }
}
