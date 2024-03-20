package com.bindord.eureka.keycloak.configuration;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimiter {
    private final int MAX_ATTEMPTS = 5;
    private Map<String, Integer> attempts = new HashMap<>();

    public void loginSucceeded(String key) {
        attempts.remove(key);
    }

    public void loginFailed(String key) {
        int current = attempts.getOrDefault(key, 0);
        attempts.put(key, current + 1);
    }

    public boolean isBlocked(String key) {
        return attempts.getOrDefault(key, 0) >= MAX_ATTEMPTS;
    }
}
