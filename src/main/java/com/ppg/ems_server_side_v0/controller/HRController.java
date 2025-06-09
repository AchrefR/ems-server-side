package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.dto.*;
import com.ppg.ems_server_side_v0.service.core.HRService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hr")
@RequiredArgsConstructor
@Slf4j
public class HRController {

    private final HRService hrService;

    // HR Dashboard
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<HRDashboardResponse> getHRDashboard() {
        try {
            HRDashboardResponse dashboard = hrService.getHRDashboard();
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            log.error("Error fetching HR dashboard: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Employee Management
    @GetMapping("/employees")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {
        try {
            Page<EmployeeResponse> employees = hrService.getAllEmployees(page, size, sortBy, sortDirection);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Error fetching employees: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable String employeeId) {
        try {
            EmployeeResponse employee = hrService.getEmployeeById(employeeId);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            log.error("Error fetching employee: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable String employeeId,
            @RequestBody EmployeeUpdateRequest request) {
        try {
            EmployeeResponse employee = hrService.updateEmployee(employeeId, request);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            log.error("Error updating employee: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/employees/{employeeId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String employeeId) {
        try {
            hrService.deleteEmployee(employeeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting employee: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/employees/search")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<Page<EmployeeResponse>> searchEmployees(
            @RequestParam("query") String searchQuery,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            Page<EmployeeResponse> employees = hrService.searchEmployees(searchQuery, page, size);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Error searching employees: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Department Assignment
    @PutMapping("/employees/{employeeId}/department/{departmentId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> assignEmployeeToDepartment(
            @PathVariable String employeeId,
            @PathVariable String departmentId) {
        try {
            EmployeeResponse employee = hrService.assignEmployeeToDepartment(employeeId, departmentId);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            log.error("Error assigning employee to department: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/departments/{departmentId}/employees")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByDepartment(@PathVariable String departmentId) {
        try {
            List<EmployeeResponse> employees = hrService.getEmployeesByDepartment(departmentId);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Error fetching employees by department: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Salary Management
    @PutMapping("/employees/{employeeId}/salary")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> updateEmployeeSalary(
            @PathVariable String employeeId,
            @RequestBody SalaryUpdateRequest request) {
        try {
            EmployeeResponse employee = hrService.updateEmployeeSalary(employeeId, request);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            log.error("Error updating employee salary: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/employees/salary-range")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesBySalaryRange(
            @RequestParam("minSalary") Double minSalary,
            @RequestParam("maxSalary") Double maxSalary) {
        try {
            List<EmployeeResponse> employees = hrService.getEmployeesBySalaryRange(minSalary, maxSalary);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Error fetching employees by salary range: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Statistics and Analytics
    @GetMapping("/statistics/employees")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getEmployeeStatistics() {
        try {
            Map<String, Object> statistics = hrService.getEmployeeStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            log.error("Error fetching employee statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statistics/performance")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getPerformanceStatistics() {
        try {
            Map<String, Object> statistics = hrService.getPerformanceStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            log.error("Error fetching performance statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statistics/leave")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getLeaveStatistics() {
        try {
            Map<String, Object> statistics = hrService.getLeaveStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            log.error("Error fetching leave statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statistics/departments")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDepartmentStatistics() {
        try {
            Map<String, Object> statistics = hrService.getDepartmentStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            log.error("Error fetching department statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Test endpoint for development
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("HR service is working!");
    }
}
