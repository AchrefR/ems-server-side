package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.JobPost;
import com.ppg.ems_server_side_v0.model.api.response.JobPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobPostMapper {
    JobPostResponse toJobPostResponse(JobPost jobPost) {
        return new JobPostResponse(
                jobPost.getId(),
                jobPost.getTitle(),
                jobPost.getDescription()
        );
    }

    List<JobPostResponse> toJobPostResponseList(List<JobPost> jobPosts) {
        List<JobPostResponse> jobPostResponseList = new ArrayList<>();
        jobPosts.forEach(jobPost -> {
            jobPostResponseList.add(toJobPostResponse(jobPost));
        });
        return jobPostResponseList;
    }
}
