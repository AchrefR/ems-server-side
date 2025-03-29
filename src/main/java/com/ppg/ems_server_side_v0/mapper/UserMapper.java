package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.model.api.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserResponse userMapper(User user) {

        return new UserResponse(user.getId(),
                user.getEmail(),
                user.getRole().getRole());
    }

    public List<UserResponse> allUsersMapper(List<User> users) {

        return users.stream().map((user)->userMapper(user)).toList();
    }
}
