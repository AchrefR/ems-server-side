package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.SalaryInformation;
import com.ppg.ems_server_side_v0.mapper.SalaryInformationMapper;
import com.ppg.ems_server_side_v0.model.api.request.SalaryInformationDTO;
import com.ppg.ems_server_side_v0.model.api.response.SalaryInformationResponse;
import com.ppg.ems_server_side_v0.repository.SalaryInformationRepository;
import com.ppg.ems_server_side_v0.service.core.SalaryInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalaryInformationServiceImpl implements SalaryInformationService {

    private final SalaryInformationRepository salaryInformationRepository;
    private final SalaryInformationMapper salaryInformationMapper;

    @Override
    public SalaryInformationResponse addSalaryInformation(SalaryInformationDTO salaryInformationDTO) {
        SalaryInformation salaryInformation = SalaryInformation.builder()
                .salary(salaryInformationDTO.salary())
                .hourlyRate(salaryInformationDTO.hourlyRate())
                .build();

        SalaryInformation savedSalaryInfo = salaryInformationRepository.save(salaryInformation);
        return salaryInformationMapper.toSalaryInformationResponse(savedSalaryInfo);
    }

    @Override
    public SalaryInformationResponse updateSalaryInformation(SalaryInformationDTO salaryInformationDTO, String id) {
        SalaryInformation salaryInformation = salaryInformationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Information salariale non trouvée"));

        if (salaryInformationDTO.salary() != null) {
            salaryInformation.setSalary(salaryInformationDTO.salary());
        }
        
        salaryInformation.setHourlyRate(salaryInformationDTO.hourlyRate());

        SalaryInformation updatedSalaryInfo = salaryInformationRepository.save(salaryInformation);
        return salaryInformationMapper.toSalaryInformationResponse(updatedSalaryInfo);
    }

    @Override
    public void deleteSalaryInformation(String id) {
        if (!salaryInformationRepository.existsById(id)) {
            throw new RuntimeException("Information salariale non trouvée");
        }
        salaryInformationRepository.deleteById(id);
    }

    @Override
    public SalaryInformationResponse findSalaryInformationById(String id) {
        SalaryInformation salaryInformation = salaryInformationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Information salariale non trouvée"));
        return salaryInformationMapper.toSalaryInformationResponse(salaryInformation);
    }

    @Override
    public List<SalaryInformationResponse> findAllSalaryInformations() {
        List<SalaryInformation> salaryInformations = salaryInformationRepository.findAll();
        return salaryInformationMapper.toSalaryInformationResponseList(salaryInformations);
    }
}