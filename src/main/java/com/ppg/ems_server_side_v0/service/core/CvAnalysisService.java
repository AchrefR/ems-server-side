package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.CvFilterRequest;
import com.ppg.ems_server_side_v0.model.api.response.CvAnalysisResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CvAnalysisService {
    
    /**
     * Analyzes multiple CV files against a job description
     */
    List<CvAnalysisResponse> analyzeCvs(List<MultipartFile> cvFiles, CvFilterRequest filterRequest);
    
    /**
     * Extracts text content from a PDF file
     */
    String extractTextFromPdf(MultipartFile file);
    
    /**
     * Calculates keyword matching score between CV text and job description
     */
    Double calculateKeywordScore(String cvText, String jobDescription);
    
    /**
     * Calculates semantic similarity score between CV text and job description
     */
    Double calculateSemanticScore(String cvText, String jobDescription);
    
    /**
     * Gets all CV analyses for a specific job description
     */
    List<CvAnalysisResponse> getCvAnalysesByJobDescription(String jobDescriptionId);
    
    /**
     * Downloads a CV file by analysis ID
     */
    byte[] downloadCvFile(String analysisId);
}
