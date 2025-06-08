package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.*;
import com.ppg.ems_server_side_v0.model.api.request.EmployeeDTO;
import com.ppg.ems_server_side_v0.model.api.response.EmployeeResponse;
import com.ppg.ems_server_side_v0.model.api.response.UserResponse;
import com.ppg.ems_server_side_v0.repository.*;
import com.ppg.ems_server_side_v0.service.core.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public EmployeeResponse addEmployee(EmployeeDTO employeeDTO) {

        Person person = this.personRepository.findById(employeeDTO.personId()).orElseThrow(()->new RuntimeException("person is not found !"));

        SalaryInformation salaryInformation = this.salaryInformationRepository.findById(employeeDTO.salaryInformationId()).orElseThrow(()->new RuntimeException("salary information is not found"));



    }

    @Override
    public EmployeeResponse updateEmployeeById(EmployeeDTO employeeDTO, String id) {
        return null;
    }

    @Override
    public EmployeeResponse deleteEmployee(String id) {
        return null;
    }

    @Override
    public EmployeeResponse findEmployeeById(String id) {
        return null;
    }

    @Override
    public List<EmployeeResponse> findAllEmployees() {
        return List.of();
    }
}
