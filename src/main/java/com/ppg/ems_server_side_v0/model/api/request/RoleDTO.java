package com.ppg.ems_server_side_v0.model.api.request;

import java.util.List;

public record RoleDTO(

        String role,

        List<String> accessesIds
) {}
