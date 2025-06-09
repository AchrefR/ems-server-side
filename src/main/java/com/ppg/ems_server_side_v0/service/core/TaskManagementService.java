package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.dto.TaskRequest;
import com.ppg.ems_server_side_v0.dto.TaskResponse;
import com.ppg.ems_server_side_v0.dto.TaskUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface TaskManagementService {
    
    // Task CRUD operations
    TaskResponse createTask(TaskRequest request, String createdById);
    TaskResponse updateTask(String taskId, TaskUpdateRequest request);
    TaskResponse getTaskById(String taskId);
    void deleteTask(String taskId);
    
    // Task listing and filtering
    Page<TaskResponse> getAllTasks(int page, int size, String sortBy, String sortDirection);
    Page<TaskResponse> getTasksByAssignedUser(String userId, int page, int size);
    Page<TaskResponse> getTasksByCreator(String creatorId, int page, int size);
    Page<TaskResponse> getTasksByStatus(String status, int page, int size);
    List<TaskResponse> searchTasks(String searchTerm);
    
    // Task status management
    TaskResponse updateTaskStatus(String taskId, String newStatus, String userId);
    TaskResponse markTaskAsCompleted(String taskId, String userId);
    TaskResponse startTask(String taskId, String userId);
    
    // Task assignment
    TaskResponse assignTask(String taskId, String assigneeId);
    TaskResponse reassignTask(String taskId, String newAssigneeId);
    
    // Kanban board operations
    List<TaskResponse> getTasksByStatusForBoard(String status);
    Map<String, List<TaskResponse>> getKanbanBoard(String userId);
    TaskResponse moveTaskToColumn(String taskId, String newStatus);
    
    // Task analytics and statistics
    Map<String, Object> getTaskStatistics(String userId);
    Map<String, Object> getOverallTaskStatistics();
    List<TaskResponse> getOverdueTasks(String userId);
    List<TaskResponse> getTasksDueSoon(String userId, int days);
    List<TaskResponse> getCompletedTasks(String userId, int page, int size);
    
    // Task filtering and advanced search
    Page<TaskResponse> getTasksWithFilters(
        String assignedToId,
        String status,
        String priority,
        String category,
        String startDate,
        String endDate,
        int page,
        int size
    );
    
    // Bulk operations
    List<TaskResponse> bulkUpdateTaskStatus(List<String> taskIds, String newStatus);
    void bulkDeleteTasks(List<String> taskIds);
    List<TaskResponse> bulkAssignTasks(List<String> taskIds, String assigneeId);
}
