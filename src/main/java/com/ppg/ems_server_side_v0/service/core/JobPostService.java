package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.JobPostDTO;
import com.ppg.ems_server_side_v0.model.api.response.JobPostResponse;

import java.util.List;

public interface JobPostService {

    JobPostResponse addJobPost(JobPostDTO jobPostDTO);

    JobPostResponse updateJobPostById(JobPostResponse jobPostResponse, String id);

    void deleteJobPostById(String id);

    JobPostResponse findJobPostById(String id);

    List<JobPostResponse> findAllJobPosts();


}
