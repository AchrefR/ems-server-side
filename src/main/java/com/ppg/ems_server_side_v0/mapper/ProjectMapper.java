package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Project;
import com.ppg.ems_server_side_v0.model.api.response.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor

public class ProjectMapper {

    ProjectResponse toProjectResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getDescription(),
                project.getDeadline(),
                project.getDepartment().getId(),
                project.getProjectLeader().getId()
        );
    }

    List<ProjectResponse> toProjectResponseList(List<Project> projectList) {
        List<ProjectResponse> projectResponseList = new ArrayList<>();
        projectList.forEach(project -> {
            projectResponseList.add(toProjectResponse(project));
        });
        return projectResponseList;
    }
}
