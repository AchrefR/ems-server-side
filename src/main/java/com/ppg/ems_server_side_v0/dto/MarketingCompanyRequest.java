package com.ppg.ems_server_side_v0.dto;

import com.ppg.ems_server_side_v0.domain.MarketingCompany;
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
public class MarketingCompanyRequest {
    
    private String companyName;
    private String industry;
    private String website;
    private String email;
    private String phone;
    private String address;
    private String contactPersonName;
    private String contactPersonTitle;
    private String contactPersonEmail;
    private String contactPersonPhone;
    private MarketingCompany.CompanySize companySize;
    private MarketingCompany.PartnershipStatus partnershipStatus;
    private MarketingCompany.CompanyRating rating;
    
    // Financial information
    private BigDecimal annualRevenue;
    private BigDecimal marketValue;
    private BigDecimal potentialDealValue;
    
    // Partnership details
    private LocalDate partnershipStartDate;
    private LocalDate lastContactDate;
    private LocalDate nextContactDate;
    
    private String services;
    private String notes;
    private String description;
    
    // Social media
    private String linkedinUrl;
    private String facebookUrl;
    private String twitterUrl;
    private String instagramUrl;
    
    // Branding
    private String logoUrl;
    private String brandColors;
    
    // Metrics
    private Integer employeeCount;
    private Integer yearsInBusiness;
    private BigDecimal satisfactionScore;
    
    // Location
    private String country;
    private String city;
    private String state;
    private String zipCode;
}
