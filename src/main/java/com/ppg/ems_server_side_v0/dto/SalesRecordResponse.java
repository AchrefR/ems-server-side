package com.ppg.ems_server_side_v0.dto;

import com.ppg.ems_server_side_v0.domain.SalesRecord;
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
public class SalesRecordResponse {
    
    private String id;
    private String clientName;
    private String clientEmail;
    private String clientPhone;
    private String clientCompany;
    private String productService;
    private BigDecimal amount;
    private BigDecimal commission;
    private SalesRecord.SalesStatus status;
    private String statusDisplayName;
    private SalesRecord.SalesPriority priority;
    private String priorityDisplayName;
    private SalesRecord.SalesSource source;
    private String sourceDisplayName;
    private LocalDate saleDate;
    private LocalDate expectedCloseDate;
    private LocalDate actualCloseDate;
    
    // Sales employee information
    private String salesEmployeeId;
    private String salesEmployeeName;
    private String salesEmployeeEmail;
    
    private String notes;
    private String description;
    
    // Financial details
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal finalAmount;
    
    // Follow-up tracking
    private Integer followUpCount;
    private LocalDate lastFollowUpDate;
    private LocalDate nextFollowUpDate;
    
    // Performance metrics
    private Integer daysToClose;
    private BigDecimal conversionRate;
    
    // Computed fields
    private Boolean isOverdue;
    private Boolean isHighValue;
    private Boolean needsFollowUp;
    private Long daysUntilExpectedClose;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
