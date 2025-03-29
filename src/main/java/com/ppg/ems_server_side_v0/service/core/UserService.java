package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.model.api.request.UserDTO;
import com.ppg.ems_server_side_v0.model.api.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse addUser(UserDTO userDTO);

    UserResponse updateUser(UserDTO userDTO, String id);

    void deleteUserById(String id);

    UserResponse findUserById(String id);

    List<UserResponse> findAllUser();

    User findUserByEmail(String email);
}
