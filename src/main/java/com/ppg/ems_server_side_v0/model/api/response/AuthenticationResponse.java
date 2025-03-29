package com.ppg.ems_server_side_v0.model.api.response;


import com.ppg.ems_server_side_v0.domain.User;

public record AuthenticationResponse(

        String token,

        UserResponse user

) {
}
