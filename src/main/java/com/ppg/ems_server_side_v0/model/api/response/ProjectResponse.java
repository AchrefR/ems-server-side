package com.ppg.ems_server_side_v0.model.api.response;

import java.util.List;

public record ProjectResponse(

        String projectId,

        String projectDescription,

        String projectDeadline,

        DepartmentResponse departmentResponse,

        EmployeeResponse projectLeader,

        List<EmployeeResponse> relatedEmployees

) {}
