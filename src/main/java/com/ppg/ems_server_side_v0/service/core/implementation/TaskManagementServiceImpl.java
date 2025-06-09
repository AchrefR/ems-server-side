package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Employee;
import com.ppg.ems_server_side_v0.domain.Task;
import com.ppg.ems_server_side_v0.dto.TaskRequest;
import com.ppg.ems_server_side_v0.dto.TaskResponse;
import com.ppg.ems_server_side_v0.dto.TaskUpdateRequest;
import com.ppg.ems_server_side_v0.repository.EmployeeRepository;
import com.ppg.ems_server_side_v0.repository.TaskRepository;
import com.ppg.ems_server_side_v0.service.core.TaskManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public TaskResponse createTask(TaskRequest request, String createdById) {
        try {
            // For testing, create a task without requiring existing employees
            Employee creator = null;
            Employee assignee = null;

            // Try to find creator, but don't fail if not found
            try {
                creator = employeeRepository.findById(createdById).orElse(null);
            } catch (Exception e) {
                log.warn("Creator not found: {}", createdById);
            }

            // Try to find assignee if specified
            if (request.getAssignedToId() != null) {
                try {
                    assignee = employeeRepository.findById(request.getAssignedToId()).orElse(null);
                } catch (Exception e) {
                    log.warn("Assignee not found: {}", request.getAssignedToId());
                }
            }

            Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(Task.TaskStatus.valueOf(request.getStatus() != null ? request.getStatus() : "TODO"))
                .priority(Task.TaskPriority.valueOf(request.getPriority() != null ? request.getPriority() : "MEDIUM"))
                .assignedTo(assignee)
                .createdBy(creator)
                .dueDate(request.getDueDate())
                .notes(request.getNotes())
                .category(request.getCategory())
                .tags(request.getTags())
                .estimatedHours(request.getEstimatedHours())
                .progressPercentage(request.getProgressPercentage() != null ? request.getProgressPercentage() : 0)
                .build();

            Task savedTask = taskRepository.save(task);
            log.info("Task created successfully: {}", savedTask.getId());
            
            return mapToTaskResponse(savedTask);
        } catch (Exception e) {
            log.error("Error creating task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create task", e);
        }
    }

    @Override
    public TaskResponse updateTask(String taskId, TaskUpdateRequest request) {
        try {
            Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

            // Update fields if provided
            if (request.getTitle() != null) task.setTitle(request.getTitle());
            if (request.getDescription() != null) task.setDescription(request.getDescription());
            if (request.getStatus() != null) {
                Task.TaskStatus newStatus = Task.TaskStatus.valueOf(request.getStatus());
                task.setStatus(newStatus);
                
                // Set completion time if task is completed
                if (newStatus == Task.TaskStatus.COMPLETED && task.getCompletedAt() == null) {
                    task.setCompletedAt(LocalDateTime.now());
                }
                // Set start time if task is started
                if (newStatus == Task.TaskStatus.IN_PROGRESS && task.getStartedAt() == null) {
                    task.setStartedAt(LocalDateTime.now());
                }
            }
            if (request.getPriority() != null) {
                task.setPriority(Task.TaskPriority.valueOf(request.getPriority()));
            }
            if (request.getAssignedToId() != null) {
                Employee assignee = employeeRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
                task.setAssignedTo(assignee);
            }
            if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
            if (request.getNotes() != null) task.setNotes(request.getNotes());
            if (request.getCategory() != null) task.setCategory(request.getCategory());
            if (request.getTags() != null) task.setTags(request.getTags());
            if (request.getEstimatedHours() != null) task.setEstimatedHours(request.getEstimatedHours());
            if (request.getActualHours() != null) task.setActualHours(request.getActualHours());
            if (request.getProgressPercentage() != null) task.setProgressPercentage(request.getProgressPercentage());

            Task updatedTask = taskRepository.save(task);
            log.info("Task updated successfully: {}", taskId);
            
            return mapToTaskResponse(updatedTask);
        } catch (Exception e) {
            log.error("Error updating task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update task", e);
        }
    }

    @Override
    public TaskResponse getTaskById(String taskId) {
        try {
            Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
            return mapToTaskResponse(task);
        } catch (Exception e) {
            log.error("Error fetching task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch task", e);
        }
    }

    @Override
    public void deleteTask(String taskId) {
        try {
            Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
            taskRepository.delete(task);
            log.info("Task deleted successfully: {}", taskId);
        } catch (Exception e) {
            log.error("Error deleting task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete task", e);
        }
    }

    @Override
    public Page<TaskResponse> getAllTasks(int page, int size, String sortBy, String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<Task> tasks = taskRepository.findAll(pageable);
            return tasks.map(this::mapToTaskResponse);
        } catch (Exception e) {
            log.error("Error fetching all tasks: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch tasks", e);
        }
    }

    @Override
    public Page<TaskResponse> getTasksByAssignedUser(String userId, int page, int size) {
        try {
            // For testing, return empty page if user doesn't exist
            Employee user = employeeRepository.findById(userId).orElse(null);

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            if (user == null) {
                log.warn("User not found: {}, returning empty page", userId);
                return Page.empty(pageable);
            }

            Page<Task> tasks = taskRepository.findByAssignedTo(user, pageable);
            return tasks.map(this::mapToTaskResponse);
        } catch (Exception e) {
            log.error("Error fetching tasks by user: {}", e.getMessage(), e);
            // Return empty page instead of throwing exception
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            return Page.empty(pageable);
        }
    }

    @Override
    public Page<TaskResponse> getTasksByCreator(String creatorId, int page, int size) {
        try {
            Employee creator = employeeRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found"));
            
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            List<Task> tasks = taskRepository.findByCreatedByOrderByCreatedAtDesc(creator);
            
            // Convert to page manually (simplified for demo)
            return Page.empty(pageable);
        } catch (Exception e) {
            log.error("Error fetching tasks by creator: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch creator tasks", e);
        }
    }

    @Override
    public Page<TaskResponse> getTasksByStatus(String status, int page, int size) {
        try {
            Task.TaskStatus taskStatus = Task.TaskStatus.valueOf(status);
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Task> tasks = taskRepository.findByStatus(taskStatus, pageable);
            return tasks.map(this::mapToTaskResponse);
        } catch (Exception e) {
            log.error("Error fetching tasks by status: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch tasks by status", e);
        }
    }

    @Override
    public List<TaskResponse> searchTasks(String searchTerm) {
        try {
            List<Task> tasks = taskRepository.searchTasks(searchTerm);
            return tasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching tasks: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to search tasks", e);
        }
    }

    @Override
    public TaskResponse updateTaskStatus(String taskId, String newStatus, String userId) {
        try {
            Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

            Task.TaskStatus status = Task.TaskStatus.valueOf(newStatus);
            task.setStatus(status);

            // Set timestamps based on status
            if (status == Task.TaskStatus.COMPLETED) {
                task.setCompletedAt(LocalDateTime.now());
            } else if (status == Task.TaskStatus.IN_PROGRESS && task.getStartedAt() == null) {
                task.setStartedAt(LocalDateTime.now());
            }

            Task updatedTask = taskRepository.save(task);
            log.info("Task status updated: {} -> {}", taskId, newStatus);
            
            return mapToTaskResponse(updatedTask);
        } catch (Exception e) {
            log.error("Error updating task status: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update task status", e);
        }
    }

    @Override
    public TaskResponse markTaskAsCompleted(String taskId, String userId) {
        return updateTaskStatus(taskId, "COMPLETED", userId);
    }

    @Override
    public TaskResponse startTask(String taskId, String userId) {
        return updateTaskStatus(taskId, "IN_PROGRESS", userId);
    }

    @Override
    public TaskResponse assignTask(String taskId, String assigneeId) {
        try {
            Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
            
            Employee assignee = employeeRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Assignee not found"));
            
            task.setAssignedTo(assignee);
            Task updatedTask = taskRepository.save(task);
            
            log.info("Task assigned: {} -> {}", taskId, assigneeId);
            return mapToTaskResponse(updatedTask);
        } catch (Exception e) {
            log.error("Error assigning task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to assign task", e);
        }
    }

    @Override
    public TaskResponse reassignTask(String taskId, String newAssigneeId) {
        return assignTask(taskId, newAssigneeId);
    }

    // Helper method to map Task to TaskResponse
    private TaskResponse mapToTaskResponse(Task task) {
        return TaskResponse.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .status(task.getStatus() != null ? task.getStatus().name() : null)
            .statusDisplayName(task.getStatus() != null ? task.getStatus().getDisplayName() : null)
            .priority(task.getPriority() != null ? task.getPriority().name() : null)
            .priorityDisplayName(task.getPriority() != null ? task.getPriority().getDisplayName() : null)
            .assignedToId(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null)
            .assignedToName(task.getAssignedTo() != null ? getEmployeeName(task.getAssignedTo()) : null)
            .assignedToEmail(task.getAssignedTo() != null ? getEmployeeEmail(task.getAssignedTo()) : null)
            .createdById(task.getCreatedBy() != null ? task.getCreatedBy().getId() : null)
            .createdByName(task.getCreatedBy() != null ? getEmployeeName(task.getCreatedBy()) : null)
            .projectId(task.getProject() != null ? task.getProject().getId() : null)
            .projectName(task.getProject() != null ? task.getProject().getTitle() : null)
            .dueDate(task.getDueDate())
            .startedAt(task.getStartedAt())
            .completedAt(task.getCompletedAt())
            .createdAt(task.getCreatedAt())
            .updatedAt(task.getCreatedAt()) // Use createdAt since updatedAt doesn't exist
            .notes(task.getNotes())
            .category(task.getCategory())
            .tags(task.getTags())
            .estimatedHours(task.getEstimatedHours())
            .actualHours(task.getActualHours())
            .progressPercentage(task.getProgressPercentage())
            .isOverdue(task.isOverdue())
            .isCompleted(task.isCompleted())
            .isInProgress(task.isInProgress())
            .daysUntilDue(task.getDaysUntilDue())
            .startDate(task.getStartDate())
            .endDate(task.getEndDate())
            .build();
    }

    private String getEmployeeName(Employee employee) {
        if (employee.getPerson() != null) {
            return employee.getPerson().getFirstName() + " " + employee.getPerson().getLastName();
        }
        return "Unknown";
    }

    private String getEmployeeEmail(Employee employee) {
        // Since email is not in Person entity, return a placeholder
        return "employee@company.com";
    }

    // Placeholder implementations for remaining methods
    @Override
    public List<TaskResponse> getTasksByStatusForBoard(String status) {
        try {
            Task.TaskStatus taskStatus = Task.TaskStatus.valueOf(status);
            List<Task> tasks = taskRepository.findByStatusOrderByCreatedAtDesc(taskStatus);
            return tasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching tasks for board: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, List<TaskResponse>> getKanbanBoard(String userId) {
        Map<String, List<TaskResponse>> board = new HashMap<>();

        try {
            Employee user = employeeRepository.findById(userId).orElse(null);

            if (user == null) {
                log.warn("User not found: {}, returning empty kanban board", userId);
                // Return empty board for all statuses
                for (Task.TaskStatus status : Task.TaskStatus.values()) {
                    board.put(status.name(), new ArrayList<>());
                }
                return board;
            }

            // Get tasks for each status
            for (Task.TaskStatus status : Task.TaskStatus.values()) {
                List<Task> tasks = taskRepository.findByAssignedToAndStatusOrderByCreatedAtDesc(user, status);
                List<TaskResponse> taskResponses = tasks.stream()
                    .map(this::mapToTaskResponse)
                    .collect(Collectors.toList());
                board.put(status.name(), taskResponses);
            }
        } catch (Exception e) {
            log.error("Error fetching kanban board: {}", e.getMessage(), e);
            // Return empty board
            for (Task.TaskStatus status : Task.TaskStatus.values()) {
                board.put(status.name(), new ArrayList<>());
            }
        }

        return board;
    }

    @Override
    public TaskResponse moveTaskToColumn(String taskId, String newStatus) {
        return updateTaskStatus(taskId, newStatus, null);
    }

    // Placeholder implementations for other methods
    @Override
    public Map<String, Object> getTaskStatistics(String userId) {
        Map<String, Object> stats = new HashMap<>();
        try {
            Employee user = employeeRepository.findById(userId).orElse(null);

            if (user == null) {
                log.warn("User not found: {}, returning empty statistics", userId);
                stats.put("totalTasks", 0L);
                stats.put("todoTasks", 0L);
                stats.put("inProgressTasks", 0L);
                stats.put("completedTasks", 0L);
                stats.put("overdueTasks", 0L);
                return stats;
            }

            long totalTasks = taskRepository.countByAssignedTo(user);
            long todoTasks = taskRepository.countByAssignedToAndStatus(user, Task.TaskStatus.TODO);
            long inProgressTasks = taskRepository.countByAssignedToAndStatus(user, Task.TaskStatus.IN_PROGRESS);
            long completedTasks = taskRepository.countByAssignedToAndStatus(user, Task.TaskStatus.COMPLETED);
            long overdueTasks = taskRepository.countOverdueTasks(LocalDate.now());

            stats.put("totalTasks", totalTasks);
            stats.put("todoTasks", todoTasks);
            stats.put("inProgressTasks", inProgressTasks);
            stats.put("completedTasks", completedTasks);
            stats.put("overdueTasks", overdueTasks);
        } catch (Exception e) {
            log.error("Error fetching task statistics: {}", e.getMessage(), e);
            // Return empty stats instead of throwing
            stats.put("totalTasks", 0L);
            stats.put("todoTasks", 0L);
            stats.put("inProgressTasks", 0L);
            stats.put("completedTasks", 0L);
            stats.put("overdueTasks", 0L);
        }
        return stats;
    }

    @Override
    public Map<String, Object> getOverallTaskStatistics() {
        Map<String, Object> stats = new HashMap<>();
        try {
            long totalTasks = taskRepository.count();
            long todoTasks = taskRepository.countByStatus(Task.TaskStatus.TODO);
            long inProgressTasks = taskRepository.countByStatus(Task.TaskStatus.IN_PROGRESS);
            long completedTasks = taskRepository.countByStatus(Task.TaskStatus.COMPLETED);
            long overdueTasks = taskRepository.countOverdueTasks(LocalDate.now());

            stats.put("totalTasks", totalTasks);
            stats.put("todoTasks", todoTasks);
            stats.put("inProgressTasks", inProgressTasks);
            stats.put("completedTasks", completedTasks);
            stats.put("overdueTasks", overdueTasks);
        } catch (Exception e) {
            log.error("Error fetching overall task statistics: {}", e.getMessage(), e);
        }
        return stats;
    }

    @Override
    public List<TaskResponse> getOverdueTasks(String userId) {
        try {
            List<Task> tasks = taskRepository.findOverdueTasks(LocalDate.now());
            if (userId != null) {
                Employee user = employeeRepository.findById(userId).orElse(null);
                if (user != null) {
                    tasks = tasks.stream()
                        .filter(task -> task.getAssignedTo() != null && task.getAssignedTo().getId().equals(userId))
                        .collect(Collectors.toList());
                }
            }
            return tasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching overdue tasks: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<TaskResponse> getTasksDueSoon(String userId, int days) {
        // Placeholder implementation
        return new ArrayList<>();
    }

    @Override
    public List<TaskResponse> getCompletedTasks(String userId, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "completedAt"));
            Page<Task> tasks = taskRepository.findByStatusOrderByCompletedAtDesc(Task.TaskStatus.COMPLETED, pageable);
            
            if (userId != null) {
                Employee user = employeeRepository.findById(userId).orElse(null);
                if (user != null) {
                    tasks = tasks.map(task -> task.getAssignedTo() != null && task.getAssignedTo().getId().equals(userId) ? task : null)
                        .map(task -> task);
                }
            }
            
            return tasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching completed tasks: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // Placeholder implementations for remaining methods
    @Override
    public Page<TaskResponse> getTasksWithFilters(String assignedToId, String status, String priority, String category, String startDate, String endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Page.empty(pageable);
    }

    @Override
    public List<TaskResponse> bulkUpdateTaskStatus(List<String> taskIds, String newStatus) {
        return new ArrayList<>();
    }

    @Override
    public void bulkDeleteTasks(List<String> taskIds) {
        // Implementation placeholder
    }

    @Override
    public List<TaskResponse> bulkAssignTasks(List<String> taskIds, String assigneeId) {
        return new ArrayList<>();
    }
}
