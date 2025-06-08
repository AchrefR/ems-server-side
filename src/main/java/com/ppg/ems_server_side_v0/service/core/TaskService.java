package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.TaskDTO;
import com.ppg.ems_server_side_v0.model.api.response.TaskResponse;

import java.util.List;

public interface TaskService {
    
    TaskResponse addTask(TaskDTO taskDTO);
    
    TaskResponse updateTask(TaskDTO taskDTO, String id);
    
    void deleteTask(String id);
    
    TaskResponse findTaskById(String id);
    
    List<TaskResponse> findAllTasks();
    
    List<TaskResponse> findTasksByProjectId(String projectId);
    
    List<TaskResponse> findTasksByStatus(String status);
}