package com.ppg.ems_server_side_v0.model.api.response;

import java.util.List;

public record RoleResponse(

        String roleId,

        String role,

        List<String> accesses
) {}
