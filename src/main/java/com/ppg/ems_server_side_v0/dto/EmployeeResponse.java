package com.ppg.ems_server_side_v0.dto;

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
public class EmployeeResponse {
    
    private String id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private LocalDate hireDate;
    private LocalDate terminationDate;
    private String position;
    private String positionId;
    
    // Department information
    private String departmentId;
    private String departmentName;
    private String departmentType;
    
    // Manager information
    private String managerId;
    private String managerName;
    
    // Salary information
    private BigDecimal salary;
    private String salaryGrade;
    private String currency;
    
    // Employment details
    private String status;
    private String employmentType;
    private Integer yearsOfService;
    
    // Emergency contact
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelation;
    
    // Performance
    private BigDecimal performanceRating;
    private LocalDate lastReviewDate;
    private LocalDate nextReviewDate;
    
    // Leave balances
    private Integer annualLeaveBalance;
    private Integer sickLeaveBalance;
    private Integer personalLeaveBalance;
    private Integer compensatoryLeaveBalance;
    
    // Additional information
    private String skills;
    private String education;
    private String certifications;
    private String notes;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    // Computed fields
    private Boolean isActive;
    private Integer totalLeaveUsed;
    private String profilePictureUrl;
}
