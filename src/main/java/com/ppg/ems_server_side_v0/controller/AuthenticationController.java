package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.AuthenticationDTO;
import com.ppg.ems_server_side_v0.model.api.response.AuthenticationResponse;
import com.ppg.ems_server_side_v0.service.core.implementation.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationDTO authenticationDTO) {

        return ResponseEntity.ok(this.authenticationServiceImpl.authenticate(authenticationDTO));

    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(Authentication authentication) {
        try {
            log.info("Logout request received");

            // Extract user information if available
            String userInfo = "unknown";
            if (authentication != null && authentication.getName() != null) {
                userInfo = authentication.getName();
                log.info("User {} is logging out", userInfo);
            }

            // Here you could add additional logout logic such as:
            // - Invalidating tokens in a token blacklist
            // - Clearing server-side sessions
            // - Logging the logout event
            // - Notifying other services

            log.info("Logout completed successfully for user: {}", userInfo);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Logout successful",
                "timestamp", System.currentTimeMillis()
            ));

        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "Logout completed with warnings",
                "error", e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }
}
