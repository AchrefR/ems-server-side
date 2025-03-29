package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.AuthenticationDTO;
import com.ppg.ems_server_side_v0.model.api.response.AuthenticationResponse;
import com.ppg.ems_server_side_v0.service.core.implementation.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationServiceImpl;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationDTO authenticationDTO) {

        return ResponseEntity.ok(this.authenticationServiceImpl.authenticate(authenticationDTO));

    }
}
