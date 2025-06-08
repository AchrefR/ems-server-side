package com.ppg.ems_server_side_v0.model.api.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record CvAnalysisResponse(
        String id,
        String fileName,
        String originalFileName,
        Double overallScore,
        Double keywordScore,
        Double semanticScore,
        Map<String, Integer> keywordMatches,
        List<String> matchedPhrases,
        LocalDateTime analyzedAt,
        Long fileSize,
        String contentType,
        String downloadUrl
) {}
