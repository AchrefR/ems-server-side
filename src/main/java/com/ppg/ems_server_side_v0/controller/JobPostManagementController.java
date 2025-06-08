package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.JobPostDTO;
import com.ppg.ems_server_side_v0.model.api.response.JobPostResponse;
import com.ppg.ems_server_side_v0.service.core.JobPostService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-posts")
@RequiredArgsConstructor
@Slf4j
public class JobPostManagementController {
    
    private final JobPostService jobPostService;

    @PostMapping
    public ResponseEntity<JobPostResponse> addJobPost(@RequestBody JobPostDTO jobPostDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobPostService.addJobPost(jobPostDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostResponse> findJobPostById(@PathVariable String id) {
        return ResponseEntity.ok(jobPostService.findJobPostById(id));
    }

    @GetMapping
    public ResponseEntity<List<JobPostResponse>> findAllJobPosts() {
        return ResponseEntity.ok(jobPostService.findAllJobPosts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobPostResponse> updateJobPost(@PathVariable String id, @RequestBody JobPostResponse jobPostResponse) {
        return ResponseEntity.ok(jobPostService.updateJobPostById(jobPostResponse, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPost(@PathVariable String id) {
        jobPostService.deleteJobPostById(id);
        return ResponseEntity.noContent().build();
    }
}