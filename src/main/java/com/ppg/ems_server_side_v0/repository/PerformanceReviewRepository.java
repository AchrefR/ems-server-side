package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Employee;
import com.ppg.ems_server_side_v0.domain.PerformanceReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, String> {
    
    // Find reviews by employee
    List<PerformanceReview> findByEmployeeOrderByReviewDateDesc(Employee employee);
    
    Page<PerformanceReview> findByEmployee(Employee employee, Pageable pageable);
    
    // Find reviews by reviewer
    List<PerformanceReview> findByReviewerOrderByReviewDateDesc(Employee reviewer);
    
    Page<PerformanceReview> findByReviewer(Employee reviewer, Pageable pageable);
    
    // Find reviews by status
    List<PerformanceReview> findByStatusOrderByReviewDateDesc(PerformanceReview.ReviewStatus status);
    
    Page<PerformanceReview> findByStatus(PerformanceReview.ReviewStatus status, Pageable pageable);
    
    // Find reviews by date range
    @Query("SELECT pr FROM PerformanceReview pr WHERE pr.reviewDate BETWEEN :startDate AND :endDate ORDER BY pr.reviewDate DESC")
    List<PerformanceReview> findByReviewDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find latest review for employee
    @Query("SELECT pr FROM PerformanceReview pr WHERE pr.employee = :employee AND pr.status = 'COMPLETED' ORDER BY pr.reviewDate DESC")
    Optional<PerformanceReview> findLatestCompletedReviewByEmployee(@Param("employee") Employee employee);
    
    // Find reviews due for completion
    @Query("SELECT pr FROM PerformanceReview pr WHERE pr.nextReviewDate <= :date AND pr.status != 'COMPLETED'")
    List<PerformanceReview> findReviewsDue(@Param("date") LocalDate date);
    
    // Performance statistics
    @Query("SELECT AVG(pr.overallRating) FROM PerformanceReview pr WHERE pr.employee.department.id = :departmentId AND pr.status = 'COMPLETED'")
    Optional<BigDecimal> getAverageRatingByDepartment(@Param("departmentId") String departmentId);
    
    @Query("SELECT AVG(pr.overallRating) FROM PerformanceReview pr WHERE pr.status = 'COMPLETED'")
    Optional<BigDecimal> getOverallAverageRating();
    
    // Count reviews by status
    long countByStatus(PerformanceReview.ReviewStatus status);
    
    // Count reviews by employee
    long countByEmployee(Employee employee);
    
    // Find reviews by review type
    List<PerformanceReview> findByReviewTypeOrderByReviewDateDesc(PerformanceReview.ReviewType reviewType);
    
    // Complex search query
    @Query("SELECT pr FROM PerformanceReview pr WHERE " +
           "(:employeeId IS NULL OR pr.employee.id = :employeeId) AND " +
           "(:reviewerId IS NULL OR pr.reviewer.id = :reviewerId) AND " +
           "(:status IS NULL OR pr.status = :status) AND " +
           "(:reviewType IS NULL OR pr.reviewType = :reviewType) AND " +
           "(:startDate IS NULL OR pr.reviewDate >= :startDate) AND " +
           "(:endDate IS NULL OR pr.reviewDate <= :endDate) " +
           "ORDER BY pr.reviewDate DESC")
    Page<PerformanceReview> findReviewsWithFilters(
        @Param("employeeId") String employeeId,
        @Param("reviewerId") String reviewerId,
        @Param("status") PerformanceReview.ReviewStatus status,
        @Param("reviewType") PerformanceReview.ReviewType reviewType,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable
    );
}
