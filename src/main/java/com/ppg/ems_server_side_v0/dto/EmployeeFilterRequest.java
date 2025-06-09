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
public class EmployeeFilterRequest {
    
    private String searchQuery;
    private List<String> departmentIds;
    private List<String> positionIds;
    private List<String> statuses;
    private List<String> employmentTypes;
    
    // Salary filters
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    
    // Date filters
    private LocalDate hireDateFrom;
    private LocalDate hireDateTo;
    
    // Performance filters
    private BigDecimal minPerformanceRating;
    private BigDecimal maxPerformanceRating;
    
    // Manager filter
    private String managerId;
    
    // Pagination and sorting
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String sortDirection = "asc";
}
