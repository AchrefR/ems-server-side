package com.ppg.ems_server_side_v0.model.api.request;

public record CvFilterRequest(
        String jobDescriptionId,
        String jobDescriptionText,
        Integer limit,
        Double keywordWeight,
        Double semanticWeight
) {
    public CvFilterRequest {
        // Set default values
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        if (keywordWeight == null) {
            keywordWeight = 0.7;
        }
        if (semanticWeight == null) {
            semanticWeight = 0.3;
        }
    }
}
