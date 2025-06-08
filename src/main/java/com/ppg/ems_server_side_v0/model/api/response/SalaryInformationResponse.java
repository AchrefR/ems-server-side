package com.ppg.ems_server_side_v0.model.api.response;

import java.math.BigDecimal;

public record SalaryInformationResponse(
        String id,
        BigDecimal salary,
        double hourlyRate
) {}