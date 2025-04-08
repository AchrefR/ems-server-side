package com.ppg.ems_server_side_v0.model.api.request;

import java.math.BigDecimal;

public record EmployeeDTO(

        PersonDTO personDto,

        SalaryInformationDTO salaryInformationDTO,

        String positionId,

        String departmentId,

        String teamId,

        String contractId
) {}
