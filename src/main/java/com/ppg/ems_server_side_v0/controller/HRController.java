package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.CvFilterRequest;
import com.ppg.ems_server_side_v0.model.api.request.JobDescriptionDTO;
import com.ppg.ems_server_side_v0.model.api.response.CvAnalysisResponse;
import com.ppg.ems_server_side_v0.model.api.response.JobDescriptionResponse;
import com.ppg.ems_server_side_v0.service.core.CvAnalysisService;
import com.ppg.ems_server_side_v0.service.core.JobDescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/hr")
@RequiredArgsConstructor
@Slf4j
public class HRController {

    private final CvAnalysisService cvAnalysisService;
    private final JobDescriptionService jobDescriptionService;

    // Job Description endpoints
    @PostMapping("/job-descriptions")
    public ResponseEntity<JobDescriptionResponse> createJobDescription(@RequestBody JobDescriptionDTO jobDescriptionDTO) {
        String userId = getCurrentUserId();
        JobDescriptionResponse response = jobDescriptionService.createJobDescription(jobDescriptionDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/job-descriptions")
    public ResponseEntity<List<JobDescriptionResponse>> getAllJobDescriptions() {
        List<JobDescriptionResponse> responses = jobDescriptionService.getAllJobDescriptions();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/job-descriptions/{id}")
    public ResponseEntity<JobDescriptionResponse> getJobDescriptionById(@PathVariable String id) {
        JobDescriptionResponse response = jobDescriptionService.getJobDescriptionById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/job-descriptions/{id}")
    public ResponseEntity<JobDescriptionResponse> updateJobDescription(
            @PathVariable String id,
            @RequestBody JobDescriptionDTO jobDescriptionDTO) {
        JobDescriptionResponse response = jobDescriptionService.updateJobDescription(id, jobDescriptionDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/job-descriptions/{id}")
    public ResponseEntity<Void> deleteJobDescription(@PathVariable String id) {
        jobDescriptionService.deleteJobDescription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/job-descriptions/department/{department}")
    public ResponseEntity<List<JobDescriptionResponse>> getJobDescriptionsByDepartment(@PathVariable String department) {
        List<JobDescriptionResponse> responses = jobDescriptionService.getJobDescriptionsByDepartment(department);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/job-descriptions/search")
    public ResponseEntity<List<JobDescriptionResponse>> searchJobDescriptions(@RequestParam String keyword) {
        List<JobDescriptionResponse> responses = jobDescriptionService.searchJobDescriptions(keyword);
        return ResponseEntity.ok(responses);
    }

    // Test endpoint for CV analysis
    @PostMapping("/cv-analysis/test")
    public ResponseEntity<String> testCvAnalysis(@RequestParam String jobDescription) {
        try {
            log.info("Testing CV analysis with job description: {}", jobDescription);

            // Test the keyword scoring algorithm
            String sampleCvText = "Experienced software engineer with 5 years of Java development. " +
                                 "Proficient in Spring Boot, React, and database management. " +
                                 "Strong problem-solving skills and experience with agile methodologies.";

            Double keywordScore = cvAnalysisService.calculateKeywordScore(sampleCvText, jobDescription);
            Double semanticScore = cvAnalysisService.calculateSemanticScore(sampleCvText, jobDescription);

            String result = String.format("Test successful! Keyword Score: %.2f, Semantic Score: %.2f",
                                         keywordScore, semanticScore);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error in test CV analysis", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // CV Analysis endpoints
    @PostMapping("/cv-analysis")
    public ResponseEntity<List<CvAnalysisResponse>> analyzeCvs(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "jobDescriptionId", required = false) String jobDescriptionId,
            @RequestParam(value = "jobDescriptionText", required = false) String jobDescriptionText,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "keywordWeight", defaultValue = "0.7") Double keywordWeight,
            @RequestParam(value = "semanticWeight", defaultValue = "0.3") Double semanticWeight) {

        log.info("Received CV analysis request with {} files", files != null ? files.size() : 0);
        log.info("Job description text: {}", jobDescriptionText);

        // Validate input
        if ((jobDescriptionId == null || jobDescriptionId.trim().isEmpty()) &&
            (jobDescriptionText == null || jobDescriptionText.trim().isEmpty())) {
            log.error("No job description provided");
            return ResponseEntity.badRequest().build();
        }

        if (files == null || files.isEmpty()) {
            log.error("No files provided");
            return ResponseEntity.badRequest().build();
        }

        CvFilterRequest filterRequest = new CvFilterRequest(
                jobDescriptionId,
                jobDescriptionText,
                limit,
                keywordWeight,
                semanticWeight
        );

        try {
            List<CvAnalysisResponse> responses = cvAnalysisService.analyzeCvs(files, filterRequest);
            log.info("Successfully analyzed {} CVs", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            log.error("Error analyzing CVs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Message", e.getMessage())
                    .build();
        }
    }

    @GetMapping("/cv-analysis/job-description/{jobDescriptionId}")
    public ResponseEntity<List<CvAnalysisResponse>> getCvAnalysesByJobDescription(@PathVariable String jobDescriptionId) {
        List<CvAnalysisResponse> responses = cvAnalysisService.getCvAnalysesByJobDescription(jobDescriptionId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/cv-analysis/{analysisId}/download")
    public ResponseEntity<byte[]> downloadCvFile(@PathVariable String analysisId) {
        try {
            byte[] fileContent = cvAnalysisService.downloadCvFile(analysisId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "cv.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);
        } catch (Exception e) {
            log.error("Error downloading CV file", e);
            return ResponseEntity.notFound().build();
        }
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // For now, return a default user ID since HR endpoints are public
            // In a real application, you would extract the user ID from the JWT token
            return "default-hr-user";
        }
        // Return default user ID for public access
        return "default-hr-user";
    }
}
