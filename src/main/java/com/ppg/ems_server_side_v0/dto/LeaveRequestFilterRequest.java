package com.ppg.ems_server_side_v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestFilterRequest {
    
    private String employeeId;
    private String approverId;
    private List<String> statuses;
    private List<String> leaveTypes;
    
    // Date filters
    private LocalDate startDateFrom;
    private LocalDate startDateTo;
    private LocalDate endDateFrom;
    private LocalDate endDateTo;
    private LocalDate createdFrom;
    private LocalDate createdTo;
    
    // Department filter
    private List<String> departmentIds;
    
    // Days filter
    private Integer minDays;
    private Integer maxDays;
    
    // Pagination and sorting
    private int page = 0;
    private int size = 10;
    private String sortBy = "createdAt";
    private String sortDirection = "desc";
}
