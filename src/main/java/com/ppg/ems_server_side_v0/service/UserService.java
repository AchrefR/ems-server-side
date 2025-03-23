package com.ppg.ems_server_side_v0.service;

import com.ppg.ems_server_side_v0.domain.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByEmail(String email);
}
