package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.InterviewDTO;
import com.ppg.ems_server_side_v0.model.api.response.InterviewResponse;
import com.ppg.ems_server_side_v0.service.core.InterviewService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
@Slf4j
public class InterviewManagementController {
    
    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<InterviewResponse> addInterview(@RequestBody InterviewDTO interviewDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(interviewService.addInterview(interviewDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponse> findInterviewById(@PathVariable String id) {
        return ResponseEntity.ok(interviewService.findInterviewById(id));
    }

    @GetMapping
    public ResponseEntity<List<InterviewResponse>> findAllInterviews() {
        return ResponseEntity.ok(interviewService.findAllInterviews());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewResponse> updateInterview(@PathVariable String id, @RequestBody InterviewDTO interviewDTO) {
        return ResponseEntity.ok(interviewService.updateInterviewById(interviewDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable String id) {
        interviewService.deleteInterviewById(id);
        return ResponseEntity.noContent().build();
    }
}