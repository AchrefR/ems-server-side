package com.ppg.ems_server_side_v0.model.api.response;

public record ApplicationResponse(

        String applicationId,

        String description,

        String appliedDate,

        String jobId,

        String personId

) {}
