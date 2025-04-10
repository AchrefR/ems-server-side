package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.mapper.UserMapper;
import com.ppg.ems_server_side_v0.model.api.request.AuthenticationDTO;
import com.ppg.ems_server_side_v0.model.api.response.AuthenticationResponse;
import com.ppg.ems_server_side_v0.model.api.response.UserResponse;
import com.ppg.ems_server_side_v0.security.JwtService;
import com.ppg.ems_server_side_v0.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserMapper userMapper;

    public AuthenticationResponse authenticate(AuthenticationDTO authenticationDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDTO.email(),
                        authenticationDTO.password()
                )
        );
        var user = userService.findUserByEmail(authenticationDTO.email());
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken, this.userMapper.toUserReponse(user));

    }
}
