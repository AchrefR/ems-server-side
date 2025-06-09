package com.ppg.ems_server_side_v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HRDashboardResponse {
    
    // Employee Statistics
    private Long totalEmployees;
    private Long activeEmployees;
    private Long inactiveEmployees;
    private Long newHiresThisMonth;
    private Long terminationsThisMonth;
    
    // Department Statistics
    private Long totalDepartments;
    private Map<String, Long> employeesByDepartment;
    private List<DepartmentStatsDto> departmentStats;
    
    // Performance Statistics
    private Long totalPerformanceReviews;
    private Long pendingPerformanceReviews;
    private Long completedPerformanceReviews;
    private BigDecimal averagePerformanceRating;
    private Map<String, Long> performanceRatingDistribution;
    
    // Leave Statistics
    private Long totalLeaveRequests;
    private Long pendingLeaveRequests;
    private Long approvedLeaveRequests;
    private Long rejectedLeaveRequests;
    private Map<String, Long> leaveRequestsByType;
    private List<EmployeeLeaveStatsDto> employeesWithLowLeaveBalance;
    
    // Salary Statistics
    private BigDecimal averageSalary;
    private BigDecimal totalPayroll;
    private Map<String, BigDecimal> averageSalaryByDepartment;
    
    // Recent Activities
    private List<RecentActivityDto> recentActivities;
    
    // Alerts and Notifications
    private List<HRAlertDto> alerts;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentStatsDto {
        private String departmentId;
        private String departmentName;
        private Long employeeCount;
        private BigDecimal averageSalary;
        private BigDecimal averagePerformanceRating;
        private Long pendingLeaveRequests;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeLeaveStatsDto {
        private String employeeId;
        private String employeeName;
        private String departmentName;
        private Integer annualLeaveBalance;
        private Integer sickLeaveBalance;
        private Integer totalLeaveBalance;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentActivityDto {
        private String id;
        private String type; // EMPLOYEE_HIRED, EMPLOYEE_TERMINATED, LEAVE_APPROVED, PERFORMANCE_REVIEW_COMPLETED
        private String description;
        private String employeeId;
        private String employeeName;
        private String timestamp;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRAlertDto {
        private String id;
        private String type; // LOW_LEAVE_BALANCE, PENDING_REVIEW, EXPIRING_CONTRACT
        private String severity; // LOW, MEDIUM, HIGH
        private String message;
        private String employeeId;
        private String employeeName;
        private String actionRequired;
        private String dueDate;
    }
}
