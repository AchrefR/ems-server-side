package com.ppg.ems_server_side_v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    
    private String id;
    private String title;
    private String description;
    private String status;
    private String statusDisplayName;
    private String priority;
    private String priorityDisplayName;
    
    // Assignment information
    private String assignedToId;
    private String assignedToName;
    private String assignedToEmail;
    private String createdById;
    private String createdByName;
    
    // Project information
    private String projectId;
    private String projectName;
    
    // Dates
    private LocalDate dueDate;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional information
    private String notes;
    private String category;
    private String tags;
    
    // Progress tracking
    private Integer estimatedHours;
    private Integer actualHours;
    private Integer progressPercentage;
    
    // Computed fields
    private Boolean isOverdue;
    private Boolean isCompleted;
    private Boolean isInProgress;
    private Long daysUntilDue;
    
    // Legacy fields for compatibility
    private String startDate;
    private String endDate;
}
