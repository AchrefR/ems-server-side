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
public class EmployeeUpdateRequest {
    
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String position;
    private String positionId;
    
    // Department assignment
    private String departmentId;
    
    // Manager assignment
    private String managerId;
    
    // Salary information
    private BigDecimal salary;
    private String salaryGrade;
    
    // Employment details
    private String status;
    private String employmentType;
    
    // Emergency contact
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;
    
    // Performance
    private BigDecimal performanceRating;
    private LocalDate nextReviewDate;
    
    // Leave balances
    private Integer annualLeaveEntitlement;
    private Integer sickLeaveEntitlement;
    private Integer personalLeaveEntitlement;
    
    // Additional information
    private String skills;
    private String education;
    private String certifications;
    private String notes;
}
