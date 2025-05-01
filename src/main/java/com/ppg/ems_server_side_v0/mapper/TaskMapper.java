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
        return new TaskResponse(
                task.getId(),
                task.getStartDate(),
                task.getEndDate(),
                task.getStatus(),
                task.getPriority(),
                task.getDescription(),
                task.getProject().getId()
        );

    }

    public List<TaskResponse> toTaskResponseList(List<Task> tasks) {
        List<TaskResponse> taskResponses = new ArrayList<>();
        tasks.forEach(task -> {
            taskResponses.add(toTaskResponse(task));
        });
        return taskResponses;
    }
}
