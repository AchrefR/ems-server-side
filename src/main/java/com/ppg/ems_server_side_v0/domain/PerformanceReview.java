package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "performance_reviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceReview {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private Employee reviewer;
    
    @Column(nullable = false)
    private LocalDate reviewPeriodStart;
    
    @Column(nullable = false)
    private LocalDate reviewPeriodEnd;
    
    @Column(nullable = false)
    private LocalDate reviewDate;
    
    @Enumerated(EnumType.STRING)
    private ReviewType reviewType;
    
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;
    
    // Performance Ratings (1-5 scale)
    @Column(precision = 3, scale = 2)
    private BigDecimal overallRating;
    
    @Column(precision = 3, scale = 2)
    private BigDecimal qualityOfWorkRating;
    
    @Column(precision = 3, scale = 2)
    private BigDecimal productivityRating;
    
    @Column(precision = 3, scale = 2)
    private BigDecimal communicationRating;
    
    @Column(precision = 3, scale = 2)
    private BigDecimal teamworkRating;
    
    @Column(precision = 3, scale = 2)
    private BigDecimal leadershipRating;
    
    @Column(precision = 3, scale = 2)
    private BigDecimal initiativeRating;
    
    // Text feedback
    @Column(columnDefinition = "TEXT")
    private String strengths;
    
    @Column(columnDefinition = "TEXT")
    private String areasForImprovement;
    
    @Column(columnDefinition = "TEXT")
    private String goals;
    
    @Column(columnDefinition = "TEXT")
    private String employeeComments;
    
    @Column(columnDefinition = "TEXT")
    private String reviewerComments;
    
    @Column(columnDefinition = "TEXT")
    private String developmentPlan;
    
    // Recommendations
    @Enumerated(EnumType.STRING)
    private PromotionRecommendation promotionRecommendation;
    
    @Enumerated(EnumType.STRING)
    private SalaryRecommendation salaryRecommendation;
    
    private BigDecimal recommendedSalaryIncrease;
    
    private LocalDate nextReviewDate;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private String createdBy;
    private String updatedBy;
    
    // Enums
    public enum ReviewType {
        ANNUAL, QUARTERLY, PROBATIONARY, PROJECT_BASED, PROMOTION, DISCIPLINARY
    }
    
    public enum ReviewStatus {
        DRAFT, PENDING_EMPLOYEE_INPUT, PENDING_MANAGER_REVIEW, PENDING_HR_APPROVAL, COMPLETED, CANCELLED
    }
    
    public enum PromotionRecommendation {
        STRONGLY_RECOMMEND, RECOMMEND, NEUTRAL, NOT_RECOMMEND, STRONGLY_NOT_RECOMMEND
    }
    
    public enum SalaryRecommendation {
        INCREASE, MAINTAIN, DECREASE, BONUS_ONLY
    }
}
