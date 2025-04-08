package com.ppg.ems_server_side_v0.model.api.request;

public record ProjectDTO(

        String title,

        String description,

        String deadline,

        String departmentId,

        String teamId,

        String projectLeaderId
) {}
