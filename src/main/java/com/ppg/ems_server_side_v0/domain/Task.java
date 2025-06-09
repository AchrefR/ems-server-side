package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "tasks")
public class Task extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private Employee assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private Employee createdBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId", referencedColumnName = "id")
    private Project project;

    private LocalDate dueDate;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // Task categorization
    private String category;
    private String tags;

    // Progress tracking
    private Integer estimatedHours;
    private Integer actualHours;
    private Integer progressPercentage;

    // Legacy fields for compatibility
    private String startDate;
    private String endDate;

    // Computed fields
    public Boolean isOverdue() {
        return dueDate != null &&
               LocalDate.now().isAfter(dueDate) &&
               status != TaskStatus.COMPLETED;
    }

    public Boolean isCompleted() {
        return status == TaskStatus.COMPLETED;
    }

    public Boolean isInProgress() {
        return status == TaskStatus.IN_PROGRESS;
    }

    public Long getDaysUntilDue() {
        if (dueDate == null) return null;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    // Enums
    public enum TaskStatus {
        TODO("To Do"),
        IN_PROGRESS("In Progress"),
        REVIEW("In Review"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled"),
        ON_HOLD("On Hold");

        private final String displayName;

        TaskStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum TaskPriority {
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High"),
        URGENT("Urgent");

        private final String displayName;

        TaskPriority(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
