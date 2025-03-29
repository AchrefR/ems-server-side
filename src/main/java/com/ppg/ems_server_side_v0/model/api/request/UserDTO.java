package com.ppg.ems_server_side_v0.model.api.request;

public record UserDTO(

        String email,

        String password,

        String role
) {}
