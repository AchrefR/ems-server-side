package com.ppg.ems_server_side_v0.dto;

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
public class SalaryUpdateRequest {
    
    private BigDecimal newSalary;
    private String salaryGrade;
    private String currency;
    private LocalDate effectiveDate;
    private String reason;
    private String comments;
    private BigDecimal bonus;
    private BigDecimal allowances;
    private BigDecimal deductions;
}
