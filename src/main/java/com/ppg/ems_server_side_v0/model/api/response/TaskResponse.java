package com.ppg.ems_server_side_v0.model.api.response;

public record TaskResponse(

        String taskId,

        String startDate,

        String endDate,

        String status,

        String priority,

        String description,

        ProjectResponse projectResponse

) {}
