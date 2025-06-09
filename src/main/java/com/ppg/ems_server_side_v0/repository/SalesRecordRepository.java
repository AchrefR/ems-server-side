package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.SalesRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesRecordRepository extends JpaRepository<SalesRecord, String> {
    
    // Find by status
    Page<SalesRecord> findByStatus(SalesRecord.SalesStatus status, Pageable pageable);
    
    // Find by sales employee
    Page<SalesRecord> findBySalesEmployeeId(String employeeId, Pageable pageable);
    
    // Find by date range
    Page<SalesRecord> findBySaleDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    // Find by client name containing
    Page<SalesRecord> findByClientNameContainingIgnoreCase(String clientName, Pageable pageable);
    
    // Find by amount range
    Page<SalesRecord> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);
    
    // Find by priority
    Page<SalesRecord> findByPriority(SalesRecord.SalesPriority priority, Pageable pageable);
    
    // Find by source
    Page<SalesRecord> findBySource(SalesRecord.SalesSource source, Pageable pageable);
    
    // Custom queries for analytics
    @Query("SELECT SUM(s.amount) FROM SalesRecord s WHERE s.status = 'CLOSED_WON'")
    BigDecimal getTotalClosedWonAmount();
    
    @Query("SELECT COUNT(s) FROM SalesRecord s WHERE s.status = 'CLOSED_WON'")
    Long getTotalClosedWonCount();
    
    @Query("SELECT COUNT(s) FROM SalesRecord s WHERE s.status = :status")
    Long getCountByStatus(@Param("status") SalesRecord.SalesStatus status);
    
    @Query("SELECT SUM(s.amount) FROM SalesRecord s WHERE s.status = 'CLOSED_WON' AND s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal getRevenueByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT s.source, COUNT(s) FROM SalesRecord s GROUP BY s.source")
    List<Object[]> getSalesBySource();
    
    @Query("SELECT s.status, COUNT(s) FROM SalesRecord s GROUP BY s.status")
    List<Object[]> getSalesByStatus();
    
    @Query("SELECT AVG(s.amount) FROM SalesRecord s WHERE s.status = 'CLOSED_WON'")
    BigDecimal getAverageClosedWonAmount();
    
    @Query("SELECT s FROM SalesRecord s WHERE s.nextFollowUpDate <= :date AND s.status NOT IN ('CLOSED_WON', 'CLOSED_LOST', 'CANCELLED')")
    List<SalesRecord> getOverdueFollowUps(@Param("date") LocalDate date);
    
    // Search functionality
    @Query("SELECT s FROM SalesRecord s WHERE " +
           "LOWER(s.clientName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.clientEmail) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.clientCompany) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.productService) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<SalesRecord> searchSalesRecords(@Param("searchTerm") String searchTerm, Pageable pageable);
}
