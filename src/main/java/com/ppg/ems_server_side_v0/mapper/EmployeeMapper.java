package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Employee;
import com.ppg.ems_server_side_v0.model.api.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    EmployeeResponse toEmployeeResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                new PersonResponse(
                        employee.getPerson().getId(),
                        employee.getPerson().getFirstName(),
                        employee.getPerson().getLastName(),
                        employee.getPerson().getBirthDate(),
                        employee.getPerson().getPhoneNumber(),
                        employee.getPerson().getPersonType(),
                        employee.getPerson().getAddress().getStreetName(),
                        employee.getPerson().getAddress().getZipCode(),
                        employee.getPerson().getAddress().getState(),
                        employee.getPerson().getAddress().getTown()),
                new PositionResponse(
                        employee.getPosition().getId(),
                        employee.getPosition().getTitle()),
                new DepartmentResponse(
                        employee.getDepartment().getId(),
                        employee.getDepartment().getDepartmentName(),
                        employee.getDepartment().getDepartmentType()),
                new UserResponse(
                        employee.getUser().getId(),
                        employee.getUser().getEmail(),
                        employee.getUser().getRole().getRole(),
                        employee.getUser().getPerson().getId()));
    }

    List<EmployeeResponse> toEmployeeResponseList(List<Employee> employees) {
        List<EmployeeResponse> employeesResponseList = new ArrayList<>();
        employees.forEach(employee -> employeesResponseList.add(toEmployeeResponse(employee)));
        return employeesResponseList;
    }

}
