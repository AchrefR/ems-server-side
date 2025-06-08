package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.EmployeeDTO;
import com.ppg.ems_server_side_v0.model.api.response.EmployeeResponse;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse addEmployee(EmployeeDTO employeeDTO);

    EmployeeResponse updateEmployeeById(EmployeeDTO employeeDTO, String id);

    EmployeeResponse deleteEmployee(String id);

    EmployeeResponse findEmployeeById(String id);

    List<EmployeeResponse> findAllEmployees();

}
