package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CvAnalysis extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_description_id", referencedColumnName = "id", nullable = false)
    private JobDescription jobDescription;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String originalFileName;

    @Column(columnDefinition = "TEXT")
    private String extractedText;

    @Column(nullable = false)
    private Double overallScore;

    @Column(nullable = false)
    private Double keywordScore;

    @Column(nullable = false)
    private Double semanticScore;

    @ElementCollection
    @CollectionTable(name = "cv_keyword_matches", joinColumns = @JoinColumn(name = "cv_analysis_id"))
    @MapKeyColumn(name = "keyword")
    @Column(name = "frequency")
    private Map<String, Integer> keywordMatches;

    @ElementCollection
    @CollectionTable(name = "cv_matched_phrases", joinColumns = @JoinColumn(name = "cv_analysis_id"))
    @Column(name = "phrase")
    private java.util.List<String> matchedPhrases;

    @Column(nullable = false)
    private LocalDateTime analyzedAt;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String contentType;

    @PrePersist
    protected void onCreate() {
        analyzedAt = LocalDateTime.now();
    }
}
