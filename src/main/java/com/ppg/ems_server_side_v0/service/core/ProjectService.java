package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.ProjectDTO;
import com.ppg.ems_server_side_v0.model.api.response.ProjectResponse;

import java.util.List;

public interface ProjectService {
    
    ProjectResponse addProject(ProjectDTO projectDTO);
    
    ProjectResponse updateProject(ProjectDTO projectDTO, String id);
    
    void deleteProject(String id);
    
    ProjectResponse findProjectById(String id);
    
    List<ProjectResponse> findAllProjects();
}