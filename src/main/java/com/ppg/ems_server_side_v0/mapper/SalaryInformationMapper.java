package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.SalaryInformation;
import com.ppg.ems_server_side_v0.model.api.response.SalaryInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SalaryInformationMapper {

    public SalaryInformationResponse toSalaryInformationResponse(SalaryInformation salaryInformation) {
        return new SalaryInformationResponse(
                salaryInformation.getId(),
                salaryInformation.getSalary(),
                salaryInformation.getHourlyRate()
        );
    }

    public List<SalaryInformationResponse> toSalaryInformationResponseList(List<SalaryInformation> salaryInformations) {
        return salaryInformations.stream()
                .map(this::toSalaryInformationResponse)
                .collect(Collectors.toList());
    }
}