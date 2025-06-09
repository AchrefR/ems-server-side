package com.ppg.ems_server_side_v0.dto;

import com.ppg.ems_server_side_v0.domain.SalesRecord;
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
public class SalesRecordRequest {
    
    private String clientName;
    private String clientEmail;
    private String clientPhone;
    private String clientCompany;
    private String productService;
    private BigDecimal amount;
    private BigDecimal commission;
    private SalesRecord.SalesStatus status;
    private SalesRecord.SalesPriority priority;
    private SalesRecord.SalesSource source;
    private LocalDate saleDate;
    private LocalDate expectedCloseDate;
    private LocalDate actualCloseDate;
    private String salesEmployeeId;
    private String notes;
    private String description;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal finalAmount;
    private Integer followUpCount;
    private LocalDate lastFollowUpDate;
    private LocalDate nextFollowUpDate;
    private Integer daysToClose;
    private BigDecimal conversionRate;
}
