package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.SalaryInformationDTO;
import com.ppg.ems_server_side_v0.model.api.response.SalaryInformationResponse;

import java.util.List;

public interface SalaryInformationService {
    
    SalaryInformationResponse addSalaryInformation(SalaryInformationDTO salaryInformationDTO);
    
    SalaryInformationResponse updateSalaryInformation(SalaryInformationDTO salaryInformationDTO, String id);
    
    void deleteSalaryInformation(String id);
    
    SalaryInformationResponse findSalaryInformationById(String id);
    
    List<SalaryInformationResponse> findAllSalaryInformations();
}