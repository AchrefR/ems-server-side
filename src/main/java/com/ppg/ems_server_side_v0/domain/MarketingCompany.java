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
@Table(name = "marketing_companies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingCompany {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String companyName;
    
    @Column(nullable = false)
    private String industry;
    
    private String website;
    
    private String email;
    
    private String phone;
    
    @Column(columnDefinition = "TEXT")
    private String address;
    
    private String contactPersonName;
    
    private String contactPersonTitle;
    
    private String contactPersonEmail;
    
    private String contactPersonPhone;
    
    @Enumerated(EnumType.STRING)
    private CompanySize companySize;
    
    @Enumerated(EnumType.STRING)
    private PartnershipStatus partnershipStatus;
    
    @Enumerated(EnumType.STRING)
    private CompanyRating rating;
    
    // Financial information
    @Column(precision = 15, scale = 2)
    private BigDecimal annualRevenue;

    @Column(precision = 15, scale = 2)
    private BigDecimal marketValue;

    @Column(precision = 15, scale = 2)
    private BigDecimal potentialDealValue;
    
    // Partnership details
    private LocalDate partnershipStartDate;
    private LocalDate lastContactDate;
    private LocalDate nextContactDate;
    
    @Column(columnDefinition = "TEXT")
    private String services;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    // Social media and online presence
    private String linkedinUrl;
    private String facebookUrl;
    private String twitterUrl;
    private String instagramUrl;
    
    // Logo and branding
    private String logoUrl;
    private String brandColors;
    
    // Performance metrics
    private Integer employeeCount;
    private Integer yearsInBusiness;

    @Column(precision = 5, scale = 2)
    private BigDecimal satisfactionScore;
    
    // Location details
    private String country;
    private String city;
    private String state;
    private String zipCode;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private String createdBy;
    private String updatedBy;
    
    // Enums
    public enum CompanySize {
        STARTUP, SMALL, MEDIUM, LARGE, ENTERPRISE
    }
    
    public enum PartnershipStatus {
        PROSPECT, CONTACTED, NEGOTIATING, PARTNER, 
        INACTIVE, TERMINATED, BLACKLISTED
    }
    
    public enum CompanyRating {
        EXCELLENT, GOOD, AVERAGE, POOR, UNRATED
    }
}
