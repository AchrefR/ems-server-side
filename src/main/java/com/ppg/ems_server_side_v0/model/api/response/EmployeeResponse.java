package com.ppg.ems_server_side_v0.model.api.response;

public record EmployeeResponse(

        String employeeId,

        PersonResponse personResponse,

        PositionResponse positionResponse,

        DepartmentResponse departmentResponse,

        UserResponse userResponse

) {}
