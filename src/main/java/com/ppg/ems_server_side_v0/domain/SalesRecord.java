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
@Table(name = "sales_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String clientName;
    
    @Column(nullable = false)
    private String clientEmail;
    
    private String clientPhone;
    
    private String clientCompany;
    
    @Column(nullable = false)
    private String productService;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal commission;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalesStatus status;
    
    @Enumerated(EnumType.STRING)
    private SalesPriority priority;
    
    @Enumerated(EnumType.STRING)
    private SalesSource source;
    
    @Column(nullable = false)
    private LocalDate saleDate;
    
    private LocalDate expectedCloseDate;
    
    private LocalDate actualCloseDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_employee_id")
    private Employee salesEmployee;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    // Financial tracking
    @Column(precision = 15, scale = 2)
    private BigDecimal discount;

    @Column(precision = 15, scale = 2)
    private BigDecimal tax;

    @Column(precision = 15, scale = 2)
    private BigDecimal finalAmount;
    
    // Lead tracking
    private Integer followUpCount;
    private LocalDate lastFollowUpDate;
    private LocalDate nextFollowUpDate;
    
    // Performance metrics
    private Integer daysToClose;

    @Column(precision = 5, scale = 2)
    private BigDecimal conversionRate;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private String createdBy;
    private String updatedBy;
    
    // Enums
    public enum SalesStatus {
        LEAD, PROSPECT, QUALIFIED, PROPOSAL_SENT, NEGOTIATION, 
        CLOSED_WON, CLOSED_LOST, ON_HOLD, CANCELLED
    }
    
    public enum SalesPriority {
        LOW, MEDIUM, HIGH, URGENT
    }
    
    public enum SalesSource {
        WEBSITE, REFERRAL, COLD_CALL, EMAIL_CAMPAIGN, SOCIAL_MEDIA,
        TRADE_SHOW, PARTNER, INFLUENCER, DIRECT_MAIL, OTHER
    }
}
