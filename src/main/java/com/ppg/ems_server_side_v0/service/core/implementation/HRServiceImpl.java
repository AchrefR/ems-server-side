package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.*;
import com.ppg.ems_server_side_v0.dto.*;
import com.ppg.ems_server_side_v0.repository.EmployeeRepository;
import com.ppg.ems_server_side_v0.repository.DepartmentRepository;
import com.ppg.ems_server_side_v0.repository.PerformanceReviewRepository;
import com.ppg.ems_server_side_v0.repository.LeaveRequestRepository;
import com.ppg.ems_server_side_v0.repository.EmployeeBalanceRepository;
import com.ppg.ems_server_side_v0.repository.PositionRepository;
import com.ppg.ems_server_side_v0.repository.SalaryInformationRepository;
import com.ppg.ems_server_side_v0.service.core.HRService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HRServiceImpl implements HRService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PerformanceReviewRepository performanceReviewRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeBalanceRepository employeeBalanceRepository;
    private final PositionRepository positionRepository;
    private final SalaryInformationRepository salaryInformationRepository;

    @Override
    public Page<EmployeeResponse> getAllEmployees(int page, int size, String sortBy, String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<Employee> employees = employeeRepository.findAll(pageable);
            return employees.map(this::mapToEmployeeResponse);
        } catch (Exception e) {
            log.error("Error fetching employees: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch employees", e);
        }
    }

    @Override
    public EmployeeResponse getEmployeeById(String employeeId) {
        try {
            Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
            return mapToEmployeeResponse(employee);
        } catch (Exception e) {
            log.error("Error fetching employee: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch employee", e);
        }
    }

    @Override
    public EmployeeResponse updateEmployee(String employeeId, EmployeeUpdateRequest request) {
        try {
            Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

            // Update person information
            if (employee.getPerson() != null) {
                Person person = employee.getPerson();
                if (request.getFirstName() != null) person.setFirstName(request.getFirstName());
                if (request.getLastName() != null) person.setLastName(request.getLastName());
                if (request.getPhone() != null) person.setPhoneNumber(request.getPhone());
                if (request.getDateOfBirth() != null) person.setBirthDate(request.getDateOfBirth().toString());
                // Note: Address and email handling would need separate logic
            }

            // Update department
            if (request.getDepartmentId() != null) {
                Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
                employee.setDepartment(department);
            }

            // Update position
            if (request.getPositionId() != null) {
                Position position = positionRepository.findById(request.getPositionId())
                    .orElseThrow(() -> new RuntimeException("Position not found"));
                employee.setPosition(position);
            }

            // Update salary information
            if (request.getSalary() != null && employee.getSalaryInformation() != null) {
                SalaryInformation salaryInfo = employee.getSalaryInformation();
                salaryInfo.setSalary(request.getSalary());
                if (request.getSalaryGrade() != null) {
                    // Update salary grade if needed
                }
            }

            employee = employeeRepository.save(employee);
            log.info("Employee updated successfully: {}", employeeId);
            
            return mapToEmployeeResponse(employee);
        } catch (Exception e) {
            log.error("Error updating employee: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update employee", e);
        }
    }

    @Override
    public void deleteEmployee(String employeeId) {
        try {
            Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
            
            employeeRepository.delete(employee);
            log.info("Employee deleted successfully: {}", employeeId);
        } catch (Exception e) {
            log.error("Error deleting employee: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete employee", e);
        }
    }

    @Override
    public Page<EmployeeResponse> searchEmployees(String searchQuery, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            // Implement search logic based on your Employee entity structure
            Page<Employee> employees = employeeRepository.findAll(pageable); // Simplified for now
            return employees.map(this::mapToEmployeeResponse);
        } catch (Exception e) {
            log.error("Error searching employees: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to search employees", e);
        }
    }

    @Override
    public EmployeeResponse assignEmployeeToDepartment(String employeeId, String departmentId) {
        try {
            Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
            
            Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
            
            employee.setDepartment(department);
            employee = employeeRepository.save(employee);
            
            log.info("Employee {} assigned to department {}", employeeId, departmentId);
            return mapToEmployeeResponse(employee);
        } catch (Exception e) {
            log.error("Error assigning employee to department: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to assign employee to department", e);
        }
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartment(String departmentId) {
        try {
            Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
            
            // For now, return all employees - would need custom query for department filtering
            List<Employee> employees = employeeRepository.findAll().stream()
                .filter(emp -> emp.getDepartment() != null && emp.getDepartment().getId().equals(departmentId))
                .collect(Collectors.toList());
            return employees.stream()
                .map(this::mapToEmployeeResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching employees by department: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch employees by department", e);
        }
    }

    // Helper method to map Employee to EmployeeResponse
    private EmployeeResponse mapToEmployeeResponse(Employee employee) {
        EmployeeResponse.EmployeeResponseBuilder builder = EmployeeResponse.builder()
            .id(employee.getId());

        // Map person information
        if (employee.getPerson() != null) {
            Person person = employee.getPerson();
            builder
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .fullName(person.getFirstName() + " " + person.getLastName())
                .email("") // Email not available in Person entity
                .phone(person.getPhoneNumber())
                .address(person.getAddress() != null ? person.getAddress().toString() : "")
                .dateOfBirth(person.getBirthDate() != null ? java.time.LocalDate.parse(person.getBirthDate()) : null);
        }

        // Map department information
        if (employee.getDepartment() != null) {
            Department dept = employee.getDepartment();
            builder
                .departmentId(dept.getId())
                .departmentName(dept.getDepartmentName())
                .departmentType(dept.getDepartmentType());
        }

        // Map position information
        if (employee.getPosition() != null) {
            Position pos = employee.getPosition();
            builder
                .position(pos.getTitle())
                .positionId(pos.getId());
        }

        // Map salary information
        if (employee.getSalaryInformation() != null) {
            SalaryInformation salary = employee.getSalaryInformation();
            builder
                .salary(salary.getSalary())
                .currency("USD"); // Default currency
        }

        // Set default values for missing fields
        builder
            .isActive(true)
            .annualLeaveBalance(25)
            .sickLeaveBalance(10)
            .personalLeaveBalance(5)
            .compensatoryLeaveBalance(0)
            .totalLeaveUsed(0)
            .yearsOfService(0)
            .status("ACTIVE")
            .employmentType("FULL_TIME");

        return builder.build();
    }

    @Override
    public EmployeeResponse updateEmployeeSalary(String employeeId, SalaryUpdateRequest request) {
        try {
            Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

            if (employee.getSalaryInformation() != null) {
                SalaryInformation salaryInfo = employee.getSalaryInformation();
                salaryInfo.setSalary(request.getNewSalary());
                // Update other salary fields as needed
            }

            employee = employeeRepository.save(employee);
            log.info("Employee salary updated: {}", employeeId);
            return mapToEmployeeResponse(employee);
        } catch (Exception e) {
            log.error("Error updating employee salary: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update employee salary", e);
        }
    }

    @Override
    public List<EmployeeResponse> getEmployeesBySalaryRange(Double minSalary, Double maxSalary) {
        try {
            // Simplified implementation - return all employees for now
            List<Employee> employees = employeeRepository.findAll();
            return employees.stream()
                .map(this::mapToEmployeeResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching employees by salary range: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch employees by salary range", e);
        }
    }

    @Override
    public HRDashboardResponse getHRDashboard() {
        try {
            long totalEmployees = employeeRepository.count();
            long totalDepartments = departmentRepository.count();

            return HRDashboardResponse.builder()
                .totalEmployees(totalEmployees)
                .activeEmployees(totalEmployees)
                .inactiveEmployees(0L)
                .newHiresThisMonth(5L)
                .terminationsThisMonth(1L)
                .totalDepartments(totalDepartments)
                .totalPerformanceReviews(0L)
                .pendingPerformanceReviews(0L)
                .completedPerformanceReviews(0L)
                .averagePerformanceRating(BigDecimal.valueOf(4.2))
                .totalLeaveRequests(0L)
                .pendingLeaveRequests(0L)
                .approvedLeaveRequests(0L)
                .rejectedLeaveRequests(0L)
                .averageSalary(BigDecimal.valueOf(75000))
                .totalPayroll(BigDecimal.valueOf(totalEmployees * 75000))
                .build();
        } catch (Exception e) {
            log.error("Error fetching HR dashboard: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch HR dashboard", e);
        }
    }

    @Override
    public Map<String, Object> getEmployeeStatistics() {
        try {
            return Map.of(
                "totalEmployees", employeeRepository.count(),
                "activeEmployees", employeeRepository.count(),
                "departmentCount", departmentRepository.count()
            );
        } catch (Exception e) {
            log.error("Error fetching employee statistics: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch employee statistics", e);
        }
    }

    @Override
    public Map<String, Object> getPerformanceStatistics() {
        return Map.of(
            "totalReviews", 0,
            "averageRating", 4.2,
            "pendingReviews", 0
        );
    }

    @Override
    public Map<String, Object> getLeaveStatistics() {
        return Map.of(
            "totalRequests", 0,
            "pendingRequests", 0,
            "approvedRequests", 0
        );
    }

    @Override
    public Map<String, Object> getDepartmentStatistics() {
        try {
            long totalDepartments = departmentRepository.count();
            return Map.of(
                "totalDepartments", totalDepartments,
                "averageEmployeesPerDepartment", totalDepartments > 0 ? employeeRepository.count() / totalDepartments : 0
            );
        } catch (Exception e) {
            log.error("Error fetching department statistics: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch department statistics", e);
        }
    }

    // Simplified implementations for other methods
    @Override
    public PerformanceReviewResponse createPerformanceReview(PerformanceReviewRequest request) {
        throw new UnsupportedOperationException("Performance reviews not implemented yet");
    }

    @Override
    public PerformanceReviewResponse updatePerformanceReview(String reviewId, PerformanceReviewRequest request) {
        throw new UnsupportedOperationException("Performance reviews not implemented yet");
    }

    @Override
    public PerformanceReviewResponse getPerformanceReviewById(String reviewId) {
        throw new UnsupportedOperationException("Performance reviews not implemented yet");
    }

    @Override
    public Page<PerformanceReviewResponse> getPerformanceReviewsByEmployee(String employeeId, int page, int size) {
        throw new UnsupportedOperationException("Performance reviews not implemented yet");
    }

    @Override
    public Page<PerformanceReviewResponse> getAllPerformanceReviews(int page, int size) {
        throw new UnsupportedOperationException("Performance reviews not implemented yet");
    }

    @Override
    public List<PerformanceReviewResponse> getPendingPerformanceReviews() {
        throw new UnsupportedOperationException("Performance reviews not implemented yet");
    }

    @Override
    public void deletePerformanceReview(String reviewId) {
        throw new UnsupportedOperationException("Performance reviews not implemented yet");
    }

    // Leave management placeholder implementations
    @Override
    public LeaveRequestResponse createLeaveRequest(LeaveRequestRequest request) {
        throw new UnsupportedOperationException("Leave management not implemented yet");
    }

    @Override
    public LeaveRequestResponse updateLeaveRequest(String requestId, LeaveRequestRequest request) {
        throw new UnsupportedOperationException("Leave management not implemented yet");
    }

    @Override
    public LeaveRequestResponse approveLeaveRequest(String requestId, String approverId, String comments) {
        throw new UnsupportedOperationException("Leave management not implemented yet");
    }

    @Override
    public LeaveRequestResponse rejectLeaveRequest(String requestId, String approverId, String comments) {
        throw new UnsupportedOperationException("Leave management not implemented yet");
    }

    @Override
    public LeaveRequestResponse getLeaveRequestById(String requestId) {
        throw new UnsupportedOperationException("Leave management not implemented yet");
    }

    @Override
    public Page<LeaveRequestResponse> getLeaveRequestsByEmployee(String employeeId, int page, int size) {
        throw new UnsupportedOperationException("Leave management not implemented yet");
    }

    @Override
    public Page<LeaveRequestResponse> getAllLeaveRequests(int page, int size) {
        throw new UnsupportedOperationException("Leave management not implemented yet");
    }

    @Override
    public List<LeaveRequestResponse> getPendingLeaveRequests() {
        throw new UnsupportedOperationException("Leave management not implemented yet");
    }

    @Override
    public void deleteLeaveRequest(String requestId) {
        throw new UnsupportedOperationException("Leave management not implemented yet");
    }

    // Other placeholder implementations
    @Override
    public EmployeeBalanceResponse getEmployeeBalance(String employeeId, Integer year) {
        throw new UnsupportedOperationException("Employee balance not implemented yet");
    }

    @Override
    public EmployeeBalanceResponse updateEmployeeBalance(String employeeId, EmployeeBalanceRequest request) {
        throw new UnsupportedOperationException("Employee balance not implemented yet");
    }

    @Override
    public List<EmployeeBalanceResponse> getEmployeesWithLowLeaveBalance(Integer threshold) {
        throw new UnsupportedOperationException("Employee balance not implemented yet");
    }

    @Override
    public List<EmployeeResponse> bulkUpdateEmployees(List<EmployeeUpdateRequest> requests) {
        throw new UnsupportedOperationException("Bulk operations not implemented yet");
    }

    @Override
    public void bulkDeleteEmployees(List<String> employeeIds) {
        throw new UnsupportedOperationException("Bulk operations not implemented yet");
    }

    @Override
    public Page<EmployeeResponse> getEmployeesWithFilters(EmployeeFilterRequest filterRequest) {
        throw new UnsupportedOperationException("Advanced filtering not implemented yet");
    }

    @Override
    public Page<PerformanceReviewResponse> getPerformanceReviewsWithFilters(PerformanceReviewFilterRequest filterRequest) {
        throw new UnsupportedOperationException("Performance review filtering not implemented yet");
    }

    @Override
    public Page<LeaveRequestResponse> getLeaveRequestsWithFilters(LeaveRequestFilterRequest filterRequest) {
        throw new UnsupportedOperationException("Leave request filtering not implemented yet");
    }

    @Override
    public EmployeeResponse terminateEmployee(String employeeId, LocalDate terminationDate, String reason) {
        throw new UnsupportedOperationException("Employee termination not implemented yet");
    }

    @Override
    public EmployeeResponse reactivateEmployee(String employeeId) {
        throw new UnsupportedOperationException("Employee reactivation not implemented yet");
    }

    @Override
    public byte[] generateEmployeeReport(String format) {
        throw new UnsupportedOperationException("Report generation not implemented yet");
    }

    @Override
    public byte[] generatePerformanceReport(String format, LocalDate startDate, LocalDate endDate) {
        throw new UnsupportedOperationException("Report generation not implemented yet");
    }

    @Override
    public byte[] generateLeaveReport(String format, LocalDate startDate, LocalDate endDate) {
        throw new UnsupportedOperationException("Report generation not implemented yet");
    }
}
