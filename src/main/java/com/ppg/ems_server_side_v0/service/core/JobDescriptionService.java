package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.JobDescriptionDTO;
import com.ppg.ems_server_side_v0.model.api.response.JobDescriptionResponse;

import java.util.List;

public interface JobDescriptionService {
    
    JobDescriptionResponse createJobDescription(JobDescriptionDTO jobDescriptionDTO, String userId);
    
    JobDescriptionResponse updateJobDescription(String id, JobDescriptionDTO jobDescriptionDTO);
    
    void deleteJobDescription(String id);
    
    JobDescriptionResponse getJobDescriptionById(String id);
    
    List<JobDescriptionResponse> getAllJobDescriptions();
    
    List<JobDescriptionResponse> getJobDescriptionsByDepartment(String department);
    
    List<JobDescriptionResponse> getJobDescriptionsByUser(String userId);
    
    List<JobDescriptionResponse> searchJobDescriptions(String keyword);
}
