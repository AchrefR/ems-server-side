package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.*;
import com.ppg.ems_server_side_v0.mapper.EmployeeMapper;
import com.ppg.ems_server_side_v0.model.api.request.EmployeeDTO;
import com.ppg.ems_server_side_v0.model.api.response.EmployeeResponse;
import com.ppg.ems_server_side_v0.repository.*;
import com.ppg.ems_server_side_v0.service.core.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final SalaryInformationRepository salaryInformationRepository;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamRepository teamRepository;
    private final ContractRepository contractRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse addEmployee(EmployeeDTO employeeDTO) {
        // Récupération des entités nécessaires
        Person person = this.personRepository.findById(employeeDTO.personId())
            .orElseThrow(() -> new RuntimeException("Person not found!"));
        
        SalaryInformation salaryInformation = this.salaryInformationRepository
            .findById(employeeDTO.salaryInformationId())
            .orElseThrow(() -> new RuntimeException("Salary information not found"));
            
        Position position = this.positionRepository.findById(employeeDTO.positionId())
            .orElseThrow(() -> new RuntimeException("Position not found"));
            
        Department department = this.departmentRepository.findById(employeeDTO.departmentId())
            .orElseThrow(() -> new RuntimeException("Department not found"));
            
        Team team = this.teamRepository.findById(employeeDTO.teamId())
            .orElseThrow(() -> new RuntimeException("Team not found"));
            
        Contract contract = this.contractRepository.findById(employeeDTO.contractId())
            .orElseThrow(() -> new RuntimeException("Contract not found"));

        // Création de l'utilisateur associé
        User user = this.userRepository.save(User.builder()
                .email(employeeDTO.email())
                .person(person)
                .build());

        // Création de l'employé
        Employee employee = Employee.builder()
                .person(person)
                .salaryInformation(salaryInformation)
                .position(position)
                .department(department)
                .team(team)
                .contract(contract)
                .user(user)
                .payChecks(new ArrayList<>())
                .attendances(new ArrayList<>())
                .interviews(new ArrayList<>())
                .managedProjects(new ArrayList<>())
                .build();

        Employee savedEmployee = this.employeeRepository.save(employee);
        return employeeMapper.toEmployeeResponse(savedEmployee);
    }

    @Override
    public EmployeeResponse updateEmployeeById(EmployeeDTO employeeDTO, String id) {
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Mise à jour des relations
        if (employeeDTO.positionId() != null) {
            Position position = this.positionRepository.findById(employeeDTO.positionId())
                    .orElseThrow(() -> new RuntimeException("Position not found"));
            employee.setPosition(position);
        }

        if (employeeDTO.departmentId() != null) {
            Department department = this.departmentRepository.findById(employeeDTO.departmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            employee.setDepartment(department);
        }

        if (employeeDTO.teamId() != null) {
            Team team = this.teamRepository.findById(employeeDTO.teamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            employee.setTeam(team);
        }

        Employee updatedEmployee = this.employeeRepository.save(employee);
        return employeeMapper.toEmployeeResponse(updatedEmployee);
    }

    @Override
    public EmployeeResponse deleteEmployee(String id) {
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        EmployeeResponse response = employeeMapper.toEmployeeResponse(employee);
        this.employeeRepository.deleteById(id);
        return response;
    }

    @Override
    public EmployeeResponse findEmployeeById(String id) {
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return employeeMapper.toEmployeeResponse(employee);
    }

    @Override
    public List<EmployeeResponse> findAllEmployees() {
        List<Employee> employees = this.employeeRepository.findAll();
        return employeeMapper.toEmployeeResponseList(employees);
    }
}