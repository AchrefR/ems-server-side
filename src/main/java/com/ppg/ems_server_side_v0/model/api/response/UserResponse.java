package com.ppg.ems_server_side_v0.model.api.response;

public record UserResponse(

        String userId,

        String email,

        String role
) { }
