package com.ppg.ems_server_side_v0.model.api.response;

import java.time.LocalDateTime;

public record JobDescriptionResponse(
        String id,
        String title,
        String description,
        String requirements,
        String responsibilities,
        String department,
        String location,
        String salaryRange,
        String experienceLevel,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdByName
) {}
