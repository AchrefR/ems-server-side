package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Employee;
import com.ppg.ems_server_side_v0.domain.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, String> {
    
    // Find leave requests by employee
    List<LeaveRequest> findByEmployeeOrderByCreatedAtDesc(Employee employee);
    
    Page<LeaveRequest> findByEmployee(Employee employee, Pageable pageable);
    
    // Find leave requests by approver
    List<LeaveRequest> findByApproverOrderByCreatedAtDesc(Employee approver);
    
    Page<LeaveRequest> findByApprover(Employee approver, Pageable pageable);
    
    // Find leave requests by status
    List<LeaveRequest> findByStatusOrderByCreatedAtDesc(LeaveRequest.LeaveStatus status);
    
    Page<LeaveRequest> findByStatus(LeaveRequest.LeaveStatus status, Pageable pageable);
    
    // Find pending leave requests
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.status = 'PENDING' ORDER BY lr.createdAt ASC")
    List<LeaveRequest> findPendingLeaveRequests();
    
    Page<LeaveRequest> findByStatusOrderByCreatedAtAsc(LeaveRequest.LeaveStatus status, Pageable pageable);
    
    // Find leave requests by date range
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.startDate <= :endDate AND lr.endDate >= :startDate ORDER BY lr.startDate")
    List<LeaveRequest> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find leave requests by employee and date range
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee = :employee AND lr.startDate <= :endDate AND lr.endDate >= :startDate ORDER BY lr.startDate")
    List<LeaveRequest> findByEmployeeAndDateRange(@Param("employee") Employee employee, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find leave requests by type
    List<LeaveRequest> findByLeaveTypeOrderByCreatedAtDesc(LeaveRequest.LeaveType leaveType);
    
    // Find leave requests by employee and type
    List<LeaveRequest> findByEmployeeAndLeaveTypeOrderByCreatedAtDesc(Employee employee, LeaveRequest.LeaveType leaveType);
    
    // Find leave requests by employee and status
    List<LeaveRequest> findByEmployeeAndStatusOrderByCreatedAtDesc(Employee employee, LeaveRequest.LeaveStatus status);
    
    // Count leave requests by status
    long countByStatus(LeaveRequest.LeaveStatus status);
    
    // Count leave requests by employee
    long countByEmployee(Employee employee);
    
    // Count leave requests by employee and status
    long countByEmployeeAndStatus(Employee employee, LeaveRequest.LeaveStatus status);
    
    // Calculate total leave days by employee and year
    @Query("SELECT COALESCE(SUM(lr.daysRequested), 0) FROM LeaveRequest lr WHERE lr.employee = :employee AND lr.status = 'APPROVED' AND YEAR(lr.startDate) = :year")
    Integer getTotalLeaveDaysByEmployeeAndYear(@Param("employee") Employee employee, @Param("year") int year);
    
    // Calculate total leave days by employee, type and year
    @Query("SELECT COALESCE(SUM(lr.daysRequested), 0) FROM LeaveRequest lr WHERE lr.employee = :employee AND lr.leaveType = :leaveType AND lr.status = 'APPROVED' AND YEAR(lr.startDate) = :year")
    Integer getTotalLeaveDaysByEmployeeTypeAndYear(@Param("employee") Employee employee, @Param("leaveType") LeaveRequest.LeaveType leaveType, @Param("year") int year);
    
    // Find overlapping leave requests
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee = :employee AND lr.status IN ('APPROVED', 'PENDING') AND lr.startDate <= :endDate AND lr.endDate >= :startDate AND lr.id != :excludeId")
    List<LeaveRequest> findOverlappingLeaveRequests(@Param("employee") Employee employee, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("excludeId") String excludeId);
    
    // Complex search query
    @Query("SELECT lr FROM LeaveRequest lr WHERE " +
           "(:employeeId IS NULL OR lr.employee.id = :employeeId) AND " +
           "(:approverId IS NULL OR lr.approver.id = :approverId) AND " +
           "(:status IS NULL OR lr.status = :status) AND " +
           "(:leaveType IS NULL OR lr.leaveType = :leaveType) AND " +
           "(:startDate IS NULL OR lr.startDate >= :startDate) AND " +
           "(:endDate IS NULL OR lr.endDate <= :endDate) " +
           "ORDER BY lr.createdAt DESC")
    Page<LeaveRequest> findLeaveRequestsWithFilters(
        @Param("employeeId") String employeeId,
        @Param("approverId") String approverId,
        @Param("status") LeaveRequest.LeaveStatus status,
        @Param("leaveType") LeaveRequest.LeaveType leaveType,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable
    );
}
