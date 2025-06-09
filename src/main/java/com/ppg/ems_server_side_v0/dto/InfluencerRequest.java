package com.ppg.ems_server_side_v0.dto;

import com.ppg.ems_server_side_v0.domain.Influencer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfluencerRequest {
    
    private String fullName;
    private String instagramHandle;
    private String email;
    private String phone;
    private Influencer.InfluencerNiche niche;
    private Influencer.InfluencerTier tier;
    private Influencer.CollaborationStatus collaborationStatus;
    
    // Instagram metrics
    private Long followersCount;
    private Long followingCount;
    private Integer postsCount;
    private BigDecimal engagementRate;
    private BigDecimal averageLikes;
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
    
    // Rates
    private BigDecimal ratePerPost;
    private BigDecimal ratePerStory;
    private BigDecimal ratePerReel;
    private String currency;
    
    // Performance
    private Influencer.InfluencerReputation reputation;
    private BigDecimal responseRate;
    private BigDecimal deliveryRate;
    private BigDecimal qualityScore;
    
    // History
    private LocalDate lastContactDate;
    private LocalDate lastCollaborationDate;
    private Integer totalCollaborations;
    private BigDecimal totalSpent;
    
    private String notes;
    private String specialties;
    private String previousBrands;
    
    // Social media
    private String youtubeUrl;
    private String tiktokUrl;
    private String twitterUrl;
    private String linkedinUrl;
    private String websiteUrl;
    
    // Content
    private String contentStyle;
    private String preferredBrands;
    private String contentLanguages;
}
