package com.ppg.ems_server_side_v0.model.api.request;

import java.math.BigDecimal;

public record SalaryInformationDTO(

        BigDecimal salary,

        double hourlyRate
) {}
