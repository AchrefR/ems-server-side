package com.ppg.ems_server_side_v0.service;

import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.model.api.request.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User addUser(UserDTO userDTO);

    User updateUser(UserDTO userDTO, String id);

    void deleteUserById(String id);

    User findUserById(String id);

    List<User> findAllUser();

    User findUserByEmail(String email);
}
