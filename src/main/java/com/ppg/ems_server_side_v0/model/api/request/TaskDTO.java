package com.ppg.ems_server_side_v0.model.api.request;

public record TaskDTO(

        String startDate,

        String endDate,

        String status,

        String priority,

        String description,

        String projectId

) {}
