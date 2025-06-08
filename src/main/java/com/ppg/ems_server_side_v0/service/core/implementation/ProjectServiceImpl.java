package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Department;
import com.ppg.ems_server_side_v0.domain.Employee;
import com.ppg.ems_server_side_v0.domain.Project;
import com.ppg.ems_server_side_v0.domain.Team;
import com.ppg.ems_server_side_v0.mapper.ProjectMapper;
import com.ppg.ems_server_side_v0.model.api.request.ProjectDTO;
import com.ppg.ems_server_side_v0.model.api.response.ProjectResponse;
import com.ppg.ems_server_side_v0.repository.DepartmentRepository;
import com.ppg.ems_server_side_v0.repository.EmployeeRepository;
import com.ppg.ems_server_side_v0.repository.ProjectRepository;
import com.ppg.ems_server_side_v0.repository.TeamRepository;
import com.ppg.ems_server_side_v0.service.core.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectResponse addProject(ProjectDTO projectDTO) {
        Department department = departmentRepository.findById(projectDTO.departmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        
        Team team = null;
        if (projectDTO.teamId() != null) {
            team = teamRepository.findById(projectDTO.teamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
        }
        
        Employee projectLeader = employeeRepository.findById(projectDTO.projectLeaderId())
                .orElseThrow(() -> new RuntimeException("Project leader not found"));

        Project project = Project.builder()
                .title(projectDTO.title())
                .description(projectDTO.description())
                .deadline(projectDTO.deadline())
                .department(department)
                .team(team)
                .projectLeader(projectLeader)
                .build();

        Project savedProject = projectRepository.save(project);
        return projectMapper.toProjectResponse(savedProject);
    }

    @Override
    public ProjectResponse updateProject(ProjectDTO projectDTO, String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (projectDTO.title() != null) {
            project.setTitle(projectDTO.title());
        }
        if (projectDTO.description() != null) {
            project.setDescription(projectDTO.description());
        }
        if (projectDTO.deadline() != null) {
            project.setDeadline(projectDTO.deadline());
        }
        if (projectDTO.departmentId() != null) {
            Department department = departmentRepository.findById(projectDTO.departmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            project.setDepartment(department);
        }
        if (projectDTO.teamId() != null) {
            Team team = teamRepository.findById(projectDTO.teamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            project.setTeam(team);
        }
        if (projectDTO.projectLeaderId() != null) {
            Employee projectLeader = employeeRepository.findById(projectDTO.projectLeaderId())
                    .orElseThrow(() -> new RuntimeException("Project leader not found"));
            project.setProjectLeader(projectLeader);
        }

        Project updatedProject = projectRepository.save(project);
        return projectMapper.toProjectResponse(updatedProject);
    }

    @Override
    public void deleteProject(String id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found");
        }
        projectRepository.deleteById(id);
    }

    @Override
    public ProjectResponse findProjectById(String id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public List<ProjectResponse> findAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projectMapper.toProjectResponseList(projects);
    }
}