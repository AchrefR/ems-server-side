package com.ppg.ems_server_side_v0.model.api.response;

public record EmployeeResponse(

        String employeeId,

        String name,

        String positionName,

        String departmentName,

        UserResponse userResponse

) {}
