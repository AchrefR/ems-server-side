package com.ppg.ems_server_side_v0.model.api.response;

import java.util.List;

public record UserResponse(

        String userId,

        String email,

        String role,

        String personId
) {}
