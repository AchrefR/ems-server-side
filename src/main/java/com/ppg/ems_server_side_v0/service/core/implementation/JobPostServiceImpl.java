package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.JobPost;
import com.ppg.ems_server_side_v0.mapper.JobPostMapper;
import com.ppg.ems_server_side_v0.model.api.request.JobPostDTO;
import com.ppg.ems_server_side_v0.model.api.response.JobPostResponse;
import com.ppg.ems_server_side_v0.repository.JobPostRepository;
import com.ppg.ems_server_side_v0.service.core.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {

    private final JobPostMapper jobPostMapper;

    private final JobPostRepository jobPostRepository;

    @Override
    public JobPostResponse addJobPost(JobPostDTO jobPostDTO) {

        JobPost jobPost = JobPost.builder().
                title(jobPostDTO.title()).
                description(jobPostDTO.description()).
                build();

        return this.jobPostMapper.toJobPostResponse(jobPost);
    }

    @Override
    public JobPostResponse updateJobPostById(JobPostResponse jobPostResponse, String id) {

        JobPost jobPost = this.jobPostRepository.findById(id).orElseThrow(() -> new RuntimeException("Job post is not found"));

        jobPost.setTitle(jobPostResponse.title());
        jobPost.setDescription(jobPostResponse.description());

        return this.jobPostMapper.toJobPostResponse(jobPost);
    }

    @Override
    public JobPostResponse findJobPostById(String id) {

        JobPost jobPost = this.jobPostRepository.findById(id).orElseThrow(() -> new RuntimeException("job post is not found"));

        return this.jobPostMapper.toJobPostResponse(jobPost);
    }

    @Override
    public List<JobPostResponse> findAllJobPosts() {

        List<JobPost> jobPosts = this.jobPostRepository.findAll();

        return this.jobPostMapper.toJobPostResponseList(jobPosts);
    }

    @Override
    public void deleteJobPostById(String id) {

        this.jobPostRepository.deleteById(id);

    }
}
