package com.ppg.ems_server_side_v0.model.api.request;

public record ApplicationDTO(

        String title,

        String description,

        String jobPostId,

        PersonDTO personDTO
) {}
