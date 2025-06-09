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
@Table(name = "influencers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Influencer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private String instagramHandle;
    
    private String email;
    
    private String phone;
    
    @Enumerated(EnumType.STRING)
    private InfluencerNiche niche;
    
    @Enumerated(EnumType.STRING)
    private InfluencerTier tier;
    
    @Enumerated(EnumType.STRING)
    private CollaborationStatus collaborationStatus;
    
    // Instagram metrics
    @Column(nullable = false)
    private Long followersCount;
    
    private Long followingCount;
    
    private Integer postsCount;

    @Column(precision = 5, scale = 2)
    private BigDecimal engagementRate;

    @Column(precision = 15, scale = 2)
    private BigDecimal averageLikes;

    @Column(precision = 15, scale = 2)
    private BigDecimal averageComments;
    
    // Demographics
    private String country;
    private String city;
    private Integer age;
    private String gender;
    private String language;
    
    // Professional details
    private String bio;
    private String profilePictureUrl;
    private Boolean isVerified;
    private Boolean isBusinessAccount;
    
    // Collaboration details
    @Column(precision = 15, scale = 2)
    private BigDecimal ratePerPost;

    @Column(precision = 15, scale = 2)
    private BigDecimal ratePerStory;

    @Column(precision = 15, scale = 2)
    private BigDecimal ratePerReel;

    private String currency;
    
    // Performance metrics
    @Enumerated(EnumType.STRING)
    private InfluencerReputation reputation;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal responseRate;

    @Column(precision = 5, scale = 2)
    private BigDecimal deliveryRate;

    @Column(precision = 5, scale = 2)
    private BigDecimal qualityScore;
    
    // Contact and collaboration history
    private LocalDate lastContactDate;
    private LocalDate lastCollaborationDate;
    private Integer totalCollaborations;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalSpent;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(columnDefinition = "TEXT")
    private String specialties;
    
    @Column(columnDefinition = "TEXT")
    private String previousBrands;
    
    // Social media links
    private String youtubeUrl;
    private String tiktokUrl;
    private String twitterUrl;
    private String linkedinUrl;
    private String websiteUrl;
    
    // Content preferences
    private String contentStyle;
    private String preferredBrands;
    private String contentLanguages;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private String createdBy;
    private String updatedBy;
    
    // Enums
    public enum InfluencerNiche {
        LIFESTYLE, FASHION, BEAUTY, FITNESS, FOOD, TRAVEL, 
        TECHNOLOGY, GAMING, BUSINESS, EDUCATION, HEALTH,
        PARENTING, PETS, SPORTS, MUSIC, ART, PHOTOGRAPHY
    }
    
    public enum InfluencerTier {
        NANO, MICRO, MID_TIER, MACRO, MEGA
    }
    
    public enum CollaborationStatus {
        PROSPECT, CONTACTED, NEGOTIATING, ACTIVE_PARTNER,
        COMPLETED, INACTIVE, BLACKLISTED
    }
    
    public enum InfluencerReputation {
        EXCELLENT, GOOD, AVERAGE, POOR, UNRATED
    }
}
