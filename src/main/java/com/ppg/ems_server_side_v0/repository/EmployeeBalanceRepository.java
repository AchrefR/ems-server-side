package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Employee;
import com.ppg.ems_server_side_v0.domain.EmployeeBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeBalanceRepository extends JpaRepository<EmployeeBalance, String> {
    
    // Find balance by employee and year
    Optional<EmployeeBalance> findByEmployeeAndYear(Employee employee, Year year);
    
    // Find all balances for an employee
    List<EmployeeBalance> findByEmployeeOrderByYearDesc(Employee employee);
    
    // Find balances for a specific year
    List<EmployeeBalance> findByYear(Year year);
    
    // Find current year balance for employee
    @Query("SELECT eb FROM EmployeeBalance eb WHERE eb.employee = :employee AND eb.year = :currentYear")
    Optional<EmployeeBalance> findCurrentYearBalance(@Param("employee") Employee employee, @Param("currentYear") Year currentYear);
    
    // Check if balance exists for employee and year
    boolean existsByEmployeeAndYear(Employee employee, Year year);
    
    // Get employees with low leave balance
    @Query("SELECT eb FROM EmployeeBalance eb WHERE eb.year = :year AND " +
           "(eb.annualLeaveEntitlement + COALESCE(eb.annualLeaveCarriedOver, 0) - COALESCE(eb.annualLeaveUsed, 0)) < :threshold")
    List<EmployeeBalance> findEmployeesWithLowAnnualLeaveBalance(@Param("year") Year year, @Param("threshold") Integer threshold);
    
    // Get total leave statistics for a year
    @Query("SELECT " +
           "SUM(COALESCE(eb.annualLeaveUsed, 0)) as totalAnnualLeaveUsed, " +
           "SUM(COALESCE(eb.sickLeaveUsed, 0)) as totalSickLeaveUsed, " +
           "SUM(COALESCE(eb.personalLeaveUsed, 0)) as totalPersonalLeaveUsed " +
           "FROM EmployeeBalance eb WHERE eb.year = :year")
    Object[] getTotalLeaveStatistics(@Param("year") Year year);
}
