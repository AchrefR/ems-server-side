package com.ppg.ems_server_side_v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    
    private String title;
    private String description;
    private String status;
    private String priority;
    private String assignedToId;
    private String projectId;
    private LocalDate dueDate;
    private String notes;
    private String category;
    private String tags;
    private Integer estimatedHours;
    private Integer progressPercentage;
}
