package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Task;
import com.ppg.ems_server_side_v0.model.api.response.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    public TaskResponse toTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus() != null ? task.getStatus().name() : null)
                .priority(task.getPriority() != null ? task.getPriority().name() : null)
                .projectId(task.getProject() != null ? task.getProject().getId() : null)
                .projectName(task.getProject() != null ? task.getProject().getTitle() : null)
                .assignedToId(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null)
                .assignedToName(task.getAssignedTo() != null ?
                    (task.getAssignedTo().getPerson() != null ?
                        task.getAssignedTo().getPerson().getFirstName() + " " + task.getAssignedTo().getPerson().getLastName()
                        : "Unknown") : null)
                .createdById(task.getCreatedBy() != null ? task.getCreatedBy().getId() : null)
                .createdByName(task.getCreatedBy() != null ?
                    (task.getCreatedBy().getPerson() != null ?
                        task.getCreatedBy().getPerson().getFirstName() + " " + task.getCreatedBy().getPerson().getLastName()
                        : "Unknown") : null)
                .dueDate(task.getDueDate())
                .startedAt(task.getStartedAt())
                .completedAt(task.getCompletedAt())
                .createdAt(task.getCreatedAt())
                .estimatedHours(task.getEstimatedHours())
                .actualHours(task.getActualHours())
                .progressPercentage(task.getProgressPercentage())
                .notes(task.getNotes())
                .category(task.getCategory())
                .isCompleted(task.getStatus() == Task.TaskStatus.COMPLETED)
                .isInProgress(task.getStatus() == Task.TaskStatus.IN_PROGRESS)
                .isOverdue(task.getDueDate() != null && task.getDueDate().isBefore(java.time.LocalDate.now()) && task.getStatus() != Task.TaskStatus.COMPLETED)
                .build();
    }

    public List<TaskResponse> toTaskResponseList(List<Task> tasks) {
        List<TaskResponse> taskResponses = new ArrayList<>();
        tasks.forEach(task -> {
            taskResponses.add(toTaskResponse(task));
        });
        return taskResponses;
    }
}
