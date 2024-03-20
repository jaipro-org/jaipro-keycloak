package com.bindord.eureka.keycloak.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private RateLimiter rateLimiter;

    public RateLimitingFilter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String key = extractKey(request);

        if (request.getRequestURI().contains("/auth") && "POST".equals(request.getMethod()) && rateLimiter.isBlocked(key)) {
            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            Map<String, String> responseMessage = new HashMap<>();
            responseMessage.put("message", "You have exceeded the maximum number of login attempts");
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseMessage));
            response.addHeader("Content-Type", "application/json");
            return;
        }

        filterChain.doFilter(request, response);

        if (request.getRequestURI().contains("/auth") && "POST".equals(request.getMethod()) && response.getStatus() == 200) {
            rateLimiter.loginSucceeded(key);
        } else if (request.getRequestURI().contains("/auth") && "POST".equals(request.getMethod())) {
            rateLimiter.loginFailed(key);
        }
    }

    private String extractKey(HttpServletRequest request) {
        // Use session ID as key
        return request.getSession(true).getId();
    }
}
