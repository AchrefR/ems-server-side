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

    public EmployeeResponse toEmployeeResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getPerson().getFirstName(),
                employee.getPosition().getTitle(),
                employee.getDepartment().getDepartmentName(),
                new UserResponse(
                        employee.getUser().getId(),
                        employee.getUser().getEmail(),
                        employee.getUser().getRole().getRole(),
                        employee.getUser().getPerson().getId()
                )
        );
    }

    public List<EmployeeResponse> toEmployeeResponseList(List<Employee> employees) {
        List<EmployeeResponse> employeesResponseList = new ArrayList<>();
        employees.forEach(employee -> employeesResponseList.add(toEmployeeResponse(employee)));
        return employeesResponseList;
    }

}
