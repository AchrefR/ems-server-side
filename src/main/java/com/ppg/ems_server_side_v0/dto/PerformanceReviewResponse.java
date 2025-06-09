package com.ppg.ems_server_side_v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceReviewResponse {
    
    private String id;
    private String employeeId;
    private String employeeName;
    private String reviewerId;
    private String reviewerName;
    
    private LocalDate reviewPeriodStart;
    private LocalDate reviewPeriodEnd;
    private LocalDate reviewDate;
    
    private String reviewType;
    private String status;
    
    // Performance Ratings
    private BigDecimal overallRating;
    private BigDecimal qualityOfWorkRating;
    private BigDecimal productivityRating;
    private BigDecimal communicationRating;
    private BigDecimal teamworkRating;
    private BigDecimal leadershipRating;
    private BigDecimal initiativeRating;
    
    // Text feedback
    private String strengths;
    private String areasForImprovement;
    private String goals;
    private String employeeComments;
    private String reviewerComments;
    private String developmentPlan;
    
    // Recommendations
    private String promotionRecommendation;
    private String salaryRecommendation;
    private BigDecimal recommendedSalaryIncrease;
    
    private LocalDate nextReviewDate;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
