package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.model.api.request.AuthenticationDTO;
import com.ppg.ems_server_side_v0.model.api.response.AuthenticationResponse;
import com.ppg.ems_server_side_v0.service.UserService;
import com.ppg.ems_server_side_v0.service.core.implementation.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationDTO authenticationDTO) {
         System.out.println("auth Test endpoint called");
        return ResponseEntity.ok(this.authenticationService.authenticate(authenticationDTO));

    }
}
