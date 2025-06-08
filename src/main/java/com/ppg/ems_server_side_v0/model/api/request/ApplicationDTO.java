package com.ppg.ems_server_side_v0.model.api.request;

public record ApplicationDTO(

        //application informations
        String title,

        String description,

        String appliedDate,

        String jobPostId,

        //person informations
        PersonDTO appliedBy
) {}
