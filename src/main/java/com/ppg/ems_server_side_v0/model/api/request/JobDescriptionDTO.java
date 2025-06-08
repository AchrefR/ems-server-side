package com.ppg.ems_server_side_v0.model.api.request;

public record JobDescriptionDTO(
        String title,
        String description,
        String requirements,
        String responsibilities,
        String department,
        String location,
        String salaryRange,
        String experienceLevel
) {}
