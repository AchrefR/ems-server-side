package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.ApplicationDTO;
import com.ppg.ems_server_side_v0.model.api.response.ApplicationResponse;
import com.ppg.ems_server_side_v0.service.core.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(@RequestBody ApplicationDTO applicationDTO) {
        return new ResponseEntity<>(applicationService.addApplication(applicationDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponse> updateApplication(@RequestBody ApplicationDTO applicationDTO, 
                                                              @PathVariable String id) {
        ApplicationResponse response = applicationService.updateApplication(applicationDTO, id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getApplicationById(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.findApplicationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable String id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAllApplications() {
        return ResponseEntity.ok(applicationService.findAllApplications());
    }
}