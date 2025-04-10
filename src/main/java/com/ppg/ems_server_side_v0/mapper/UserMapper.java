package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.model.api.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleMapper roleMapper;

    private final PersonMapper personMapper;

    public UserResponse toUserReponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().getRole(),
                user.getPerson().getId()
        );
    }

    public List<UserResponse> toUserReponseList(List<User> users) {

        return users.stream().map((user) -> toUserReponse(user)).toList();
    }
}
