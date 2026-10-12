package com.tanmaysharma.designledger.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    private final byte[] expectedApiKey;

    public ApiKeyAuthenticationFilter(@Value("${designledger.security.api-key}") String apiKey) {
        this.expectedApiKey = apiKey.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        byte[] provided = request.getHeader("X-API-Key") == null
                ? new byte[0]
                : request.getHeader("X-API-Key").getBytes(StandardCharsets.UTF_8);
        if (!MessageDigest.isEqual(expectedApiKey, provided)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"status\":401,\"message\":\"Missing or invalid API key\"}");
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator/health") || path.startsWith("/actuator/prometheus")
                || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
    }
}
