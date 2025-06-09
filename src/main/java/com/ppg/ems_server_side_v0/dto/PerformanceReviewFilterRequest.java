package com.ppg.ems_server_side_v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceReviewFilterRequest {
    
    private String employeeId;
    private String reviewerId;
    private List<String> statuses;
    private List<String> reviewTypes;
    
    // Date filters
    private LocalDate reviewDateFrom;
    private LocalDate reviewDateTo;
    private LocalDate periodStartFrom;
    private LocalDate periodStartTo;
    
    // Rating filters
    private BigDecimal minOverallRating;
    private BigDecimal maxOverallRating;
    
    // Department filter
    private List<String> departmentIds;
    
    // Pagination and sorting
    private int page = 0;
    private int size = 10;
    private String sortBy = "reviewDate";
    private String sortDirection = "desc";
}
