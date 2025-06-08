package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.JobDescription;
import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.model.api.request.JobDescriptionDTO;
import com.ppg.ems_server_side_v0.model.api.response.JobDescriptionResponse;
import com.ppg.ems_server_side_v0.repository.JobDescriptionRepository;
import com.ppg.ems_server_side_v0.repository.UserRepository;
import com.ppg.ems_server_side_v0.service.core.JobDescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobDescriptionServiceImpl implements JobDescriptionService {

    private final JobDescriptionRepository jobDescriptionRepository;
    private final UserRepository userRepository;

    @Override
    public JobDescriptionResponse createJobDescription(JobDescriptionDTO jobDescriptionDTO, String userId) {
        // Try to find user, but don't fail if not found (for demo purposes)
        User user = userRepository.findById(userId).orElse(null);

        JobDescription jobDescription = JobDescription.builder()
                .title(jobDescriptionDTO.title())
                .description(jobDescriptionDTO.description())
                .requirements(jobDescriptionDTO.requirements())
                .responsibilities(jobDescriptionDTO.responsibilities())
                .department(jobDescriptionDTO.department())
                .location(jobDescriptionDTO.location())
                .salaryRange(jobDescriptionDTO.salaryRange())
                .experienceLevel(jobDescriptionDTO.experienceLevel())
                .createdBy(user) // Can be null for demo purposes
                .build();

        jobDescription = jobDescriptionRepository.save(jobDescription);
        return mapToResponse(jobDescription);
    }

    @Override
    public JobDescriptionResponse updateJobDescription(String id, JobDescriptionDTO jobDescriptionDTO) {
        JobDescription jobDescription = jobDescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job description not found"));

        jobDescription.setTitle(jobDescriptionDTO.title());
        jobDescription.setDescription(jobDescriptionDTO.description());
        jobDescription.setRequirements(jobDescriptionDTO.requirements());
        jobDescription.setResponsibilities(jobDescriptionDTO.responsibilities());
        jobDescription.setDepartment(jobDescriptionDTO.department());
        jobDescription.setLocation(jobDescriptionDTO.location());
        jobDescription.setSalaryRange(jobDescriptionDTO.salaryRange());
        jobDescription.setExperienceLevel(jobDescriptionDTO.experienceLevel());

        jobDescription = jobDescriptionRepository.save(jobDescription);
        return mapToResponse(jobDescription);
    }

    @Override
    public void deleteJobDescription(String id) {
        if (!jobDescriptionRepository.existsById(id)) {
            throw new RuntimeException("Job description not found");
        }
        jobDescriptionRepository.deleteById(id);
    }

    @Override
    public JobDescriptionResponse getJobDescriptionById(String id) {
        JobDescription jobDescription = jobDescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job description not found"));
        return mapToResponse(jobDescription);
    }

    @Override
    public List<JobDescriptionResponse> getAllJobDescriptions() {
        return jobDescriptionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobDescriptionResponse> getJobDescriptionsByDepartment(String department) {
        return jobDescriptionRepository.findByDepartmentOrderByCreatedAtDesc(department)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobDescriptionResponse> getJobDescriptionsByUser(String userId) {
        return jobDescriptionRepository.findByCreatedByIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobDescriptionResponse> searchJobDescriptions(String keyword) {
        return jobDescriptionRepository.findByKeyword(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private JobDescriptionResponse mapToResponse(JobDescription jobDescription) {
        String createdByName = null;
        if (jobDescription.getCreatedBy() != null &&
            jobDescription.getCreatedBy().getPerson() != null) {
            createdByName = jobDescription.getCreatedBy().getPerson().getFirstName() + " " +
                           jobDescription.getCreatedBy().getPerson().getLastName();
        } else {
            createdByName = "HR System";
        }

        return new JobDescriptionResponse(
                jobDescription.getId(),
                jobDescription.getTitle(),
                jobDescription.getDescription(),
                jobDescription.getRequirements(),
                jobDescription.getResponsibilities(),
                jobDescription.getDepartment(),
                jobDescription.getLocation(),
                jobDescription.getSalaryRange(),
                jobDescription.getExperienceLevel(),
                jobDescription.getCreatedAt(),
                jobDescription.getUpdatedAt(),
                createdByName
        );
    }
}
