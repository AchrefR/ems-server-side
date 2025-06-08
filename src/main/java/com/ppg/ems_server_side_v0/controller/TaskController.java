package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.TaskDTO;
import com.ppg.ems_server_side_v0.model.api.response.TaskResponse;
import com.ppg.ems_server_side_v0.service.core.TaskService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    @PostMapping

    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<>(
                taskService.addTask(taskDTO),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")

    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable String id,
            @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(
                taskService.updateTask(taskDTO, id)
        );
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")

    public ResponseEntity<TaskResponse> getTaskById(@PathVariable String id) {
        return ResponseEntity.ok(
                taskService.findTaskById(id)
        );
    }

    @GetMapping

    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(
                taskService.findAllTasks()
        );
    }

    @GetMapping("/project/{projectId}")

    public ResponseEntity<List<TaskResponse>> getTasksByProjectId(
            @PathVariable String projectId) {
        return ResponseEntity.ok(
                taskService.findTasksByProjectId(projectId)
        );
    }

    @GetMapping("/status/{status}")

    public ResponseEntity<List<TaskResponse>> getTasksByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(
                taskService.findTasksByStatus(status)
        );
    }
}