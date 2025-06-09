package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.dto.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HRService {
    
    // Employee Management
    Page<EmployeeResponse> getAllEmployees(int page, int size, String sortBy, String sortDirection);
    EmployeeResponse getEmployeeById(String employeeId);
    EmployeeResponse updateEmployee(String employeeId, EmployeeUpdateRequest request);
    void deleteEmployee(String employeeId);
    Page<EmployeeResponse> searchEmployees(String searchQuery, int page, int size);
    
    // Department Management
    EmployeeResponse assignEmployeeToDepartment(String employeeId, String departmentId);
    List<EmployeeResponse> getEmployeesByDepartment(String departmentId);
    
    // Salary Management
    EmployeeResponse updateEmployeeSalary(String employeeId, SalaryUpdateRequest request);
    List<EmployeeResponse> getEmployeesBySalaryRange(Double minSalary, Double maxSalary);
    
    // Performance Management
    PerformanceReviewResponse createPerformanceReview(PerformanceReviewRequest request);
    PerformanceReviewResponse updatePerformanceReview(String reviewId, PerformanceReviewRequest request);
    PerformanceReviewResponse getPerformanceReviewById(String reviewId);
    Page<PerformanceReviewResponse> getPerformanceReviewsByEmployee(String employeeId, int page, int size);
    Page<PerformanceReviewResponse> getAllPerformanceReviews(int page, int size);
    List<PerformanceReviewResponse> getPendingPerformanceReviews();
    void deletePerformanceReview(String reviewId);
    
    // Leave Management
    LeaveRequestResponse createLeaveRequest(LeaveRequestRequest request);
    LeaveRequestResponse updateLeaveRequest(String requestId, LeaveRequestRequest request);
    LeaveRequestResponse approveLeaveRequest(String requestId, String approverId, String comments);
    LeaveRequestResponse rejectLeaveRequest(String requestId, String approverId, String comments);
    LeaveRequestResponse getLeaveRequestById(String requestId);
    Page<LeaveRequestResponse> getLeaveRequestsByEmployee(String employeeId, int page, int size);
    Page<LeaveRequestResponse> getAllLeaveRequests(int page, int size);
    List<LeaveRequestResponse> getPendingLeaveRequests();
    void deleteLeaveRequest(String requestId);
    
    // Leave Balance Management
    EmployeeBalanceResponse getEmployeeBalance(String employeeId, Integer year);
    EmployeeBalanceResponse updateEmployeeBalance(String employeeId, EmployeeBalanceRequest request);
    List<EmployeeBalanceResponse> getEmployeesWithLowLeaveBalance(Integer threshold);
    
    // HR Analytics and Reports
    HRDashboardResponse getHRDashboard();
    Map<String, Object> getEmployeeStatistics();
    Map<String, Object> getPerformanceStatistics();
    Map<String, Object> getLeaveStatistics();
    Map<String, Object> getDepartmentStatistics();
    
    // Bulk Operations
    List<EmployeeResponse> bulkUpdateEmployees(List<EmployeeUpdateRequest> requests);
    void bulkDeleteEmployees(List<String> employeeIds);
    
    // Advanced Search and Filtering
    Page<EmployeeResponse> getEmployeesWithFilters(EmployeeFilterRequest filterRequest);
    Page<PerformanceReviewResponse> getPerformanceReviewsWithFilters(PerformanceReviewFilterRequest filterRequest);
    Page<LeaveRequestResponse> getLeaveRequestsWithFilters(LeaveRequestFilterRequest filterRequest);
    
    // Employee Lifecycle
    EmployeeResponse terminateEmployee(String employeeId, LocalDate terminationDate, String reason);
    EmployeeResponse reactivateEmployee(String employeeId);
    
    // Reporting
    byte[] generateEmployeeReport(String format); // PDF, Excel
    byte[] generatePerformanceReport(String format, LocalDate startDate, LocalDate endDate);
    byte[] generateLeaveReport(String format, LocalDate startDate, LocalDate endDate);
}
