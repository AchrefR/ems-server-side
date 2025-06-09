package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.dto.TaskRequest;
import com.ppg.ems_server_side_v0.dto.TaskResponse;
import com.ppg.ems_server_side_v0.dto.TaskUpdateRequest;
import com.ppg.ems_server_side_v0.service.core.TaskManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/task-management")
@RequiredArgsConstructor
@Slf4j
public class TaskManagementController {

    private final TaskManagementService taskManagementService;

    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Task Management service is working!");
    }

    // Test POST endpoint
    @PostMapping("/test")
    public ResponseEntity<String> testPostEndpoint(@RequestBody(required = false) String body) {
        log.info("POST test endpoint called with body: {}", body);
        return ResponseEntity.ok("POST endpoint is working! Body: " + body);
    }

    // Task CRUD operations
    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        try {
            log.info("Received task creation request: {}", request);
            log.info("Request title: {}", request.getTitle());
            log.info("Request description: {}", request.getDescription());
            log.info("Request priority: {}", request.getPriority());
            log.info("Request dueDate: {}", request.getDueDate());

            // For now, use a default user ID for testing
            String userId = "default-user-id";
            TaskResponse response = taskManagementService.createTask(request, userId);
            log.info("Task created successfully: {}", response);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating task: {}", e.getMessage(), e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<TaskResponse> getTask(@PathVariable String taskId) {
        try {
            TaskResponse response = taskManagementService.getTaskById(taskId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching task: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable String taskId,
            @RequestBody TaskUpdateRequest request) {
        try {
            TaskResponse response = taskManagementService.updateTask(taskId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating task: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
        try {
            taskManagementService.deleteTask(taskId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting task: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Task listing and filtering
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Page<TaskResponse> response = taskManagementService.getAllTasks(page, size, sortBy, sortDirection);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching all tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Page.empty());
        }
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<Page<TaskResponse>> getMyTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // For now, use a default user ID for testing
            String userId = "default-user-id";
            Page<TaskResponse> response = taskManagementService.getTasksByAssignedUser(userId, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching user tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Page.empty());
        }
    }

    @GetMapping("/created-by-me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Page<TaskResponse>> getTasksCreatedByMe(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        try {
            String userId = getUserIdFromAuthentication(authentication);
            Page<TaskResponse> response = taskManagementService.getTasksByCreator(userId, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching created tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Page<TaskResponse>> getTasksByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<TaskResponse> response = taskManagementService.getTasksByStatus(status, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching tasks by status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<TaskResponse>> searchTasks(@RequestParam String q) {
        try {
            List<TaskResponse> response = taskManagementService.searchTasks(q);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Task status management
    @PutMapping("/{taskId}/status")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable String taskId,
            @RequestParam String status,
            Authentication authentication) {
        try {
            String userId = getUserIdFromAuthentication(authentication);
            TaskResponse response = taskManagementService.updateTaskStatus(taskId, status, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating task status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{taskId}/complete")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<TaskResponse> markTaskAsCompleted(
            @PathVariable String taskId,
            Authentication authentication) {
        try {
            String userId = getUserIdFromAuthentication(authentication);
            TaskResponse response = taskManagementService.markTaskAsCompleted(taskId, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error completing task: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{taskId}/start")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<TaskResponse> startTask(
            @PathVariable String taskId,
            Authentication authentication) {
        try {
            String userId = getUserIdFromAuthentication(authentication);
            TaskResponse response = taskManagementService.startTask(taskId, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error starting task: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Task assignment
    @PutMapping("/{taskId}/assign")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<TaskResponse> assignTask(
            @PathVariable String taskId,
            @RequestParam String assigneeId) {
        try {
            TaskResponse response = taskManagementService.assignTask(taskId, assigneeId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error assigning task: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Kanban board operations
    @GetMapping("/kanban")
    public ResponseEntity<Map<String, List<TaskResponse>>> getKanbanBoard() {
        try {
            // For now, use a default user ID for testing
            String userId = "default-user-id";
            Map<String, List<TaskResponse>> response = taskManagementService.getKanbanBoard(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching kanban board: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of());
        }
    }

    @GetMapping("/kanban/{status}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<TaskResponse>> getTasksForKanbanColumn(@PathVariable String status) {
        try {
            List<TaskResponse> response = taskManagementService.getTasksByStatusForBoard(status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching tasks for kanban column: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{taskId}/move")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<TaskResponse> moveTaskToColumn(
            @PathVariable String taskId,
            @RequestParam String status) {
        try {
            TaskResponse response = taskManagementService.moveTaskToColumn(taskId, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error moving task: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Task analytics and statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getTaskStatistics() {
        try {
            // For now, use a default user ID for testing
            String userId = "default-user-id";
            Map<String, Object> response = taskManagementService.getTaskStatistics(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching task statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of());
        }
    }

    @GetMapping("/statistics/overall")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Map<String, Object>> getOverallTaskStatistics() {
        try {
            Map<String, Object> response = taskManagementService.getOverallTaskStatistics();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching overall task statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<TaskResponse>> getOverdueTasks(Authentication authentication) {
        try {
            String userId = getUserIdFromAuthentication(authentication);
            List<TaskResponse> response = taskManagementService.getOverdueTasks(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching overdue tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/completed")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<TaskResponse>> getCompletedTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        try {
            String userId = getUserIdFromAuthentication(authentication);
            List<TaskResponse> response = taskManagementService.getCompletedTasks(userId, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching completed tasks: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Helper method to extract user ID from authentication
    private String getUserIdFromAuthentication(Authentication authentication) {
        try {
            // Extract user ID from JWT token or authentication principal
            if (authentication != null && authentication.getPrincipal() != null) {
                String principal = authentication.getName();
                log.debug("Authentication principal: {}", principal);

                // If the principal is already a user ID, return it
                if (principal != null && !principal.isEmpty()) {
                    return principal;
                }
            }

            // Fallback: try to get from authentication details
            if (authentication != null && authentication.getDetails() != null) {
                log.debug("Authentication details: {}", authentication.getDetails());
            }

            // If we can't extract user ID, throw an exception
            throw new RuntimeException("Unable to extract user ID from authentication");
        } catch (Exception e) {
            log.error("Error extracting user ID from authentication: {}", e.getMessage(), e);
            throw new RuntimeException("Authentication error: " + e.getMessage());
        }
    }
}
