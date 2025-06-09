package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Project;
import com.ppg.ems_server_side_v0.domain.Task;
import com.ppg.ems_server_side_v0.mapper.TaskMapper;
import com.ppg.ems_server_side_v0.model.api.request.TaskDTO;
import com.ppg.ems_server_side_v0.model.api.response.TaskResponse;
import com.ppg.ems_server_side_v0.repository.ProjectRepository;
import com.ppg.ems_server_side_v0.repository.TaskRepository;
import com.ppg.ems_server_side_v0.service.core.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskResponse addTask(TaskDTO taskDTO) {
        Project project = projectRepository.findById(taskDTO.projectId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        Task task = Task.builder()
                .startDate(taskDTO.startDate())
                .endDate(taskDTO.endDate())
                .status(taskDTO.status())
                .priority(taskDTO.priority())
                .description(taskDTO.description())
                .project(project)
                .build();

        Task savedTask = taskRepository.save(task);
        return taskMapper.toTaskResponse(savedTask);
    }

    @Override
    public TaskResponse updateTask(TaskDTO taskDTO, String id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

        if (taskDTO.projectId() != null && !taskDTO.projectId().equals(existingTask.getProject().getId())) {
            Project newProject = projectRepository.findById(taskDTO.projectId())
                    .orElseThrow(() -> new RuntimeException("Nouveau projet non trouvé"));
            existingTask.setProject(newProject);
        }

        existingTask.setStartDate(taskDTO.startDate());
        existingTask.setEndDate(taskDTO.endDate());
        existingTask.setStatus(taskDTO.status());
        existingTask.setPriority(taskDTO.priority());
        existingTask.setDescription(taskDTO.description());

        Task updatedTask = taskRepository.save(existingTask);
        return taskMapper.toTaskResponse(updatedTask);
    }

    @Override
    public void deleteTask(String id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Tâche non trouvée");
        }
        taskRepository.deleteById(id);
    }

    @Override
    public TaskResponse findTaskById(String id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
        return taskMapper.toTaskResponse(task);
    }

    @Override
    public List<TaskResponse> findAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toTaskResponseList(tasks);
    }

    @Override
    public List<TaskResponse> findTasksByProjectId(String projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return taskMapper.toTaskResponseList(tasks);
    }

    @Override
    public List<TaskResponse> findTasksByStatus(String status) {
        List<Task> tasks = taskRepository.findByStatusString(status);
        return taskMapper.toTaskResponseList(tasks);
    }
}