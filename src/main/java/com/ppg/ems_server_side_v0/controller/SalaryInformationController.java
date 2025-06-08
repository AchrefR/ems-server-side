package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.SalaryInformationDTO;
import com.ppg.ems_server_side_v0.model.api.response.SalaryInformationResponse;
import com.ppg.ems_server_side_v0.service.core.SalaryInformationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salary-informations")
@RequiredArgsConstructor

@CrossOrigin(origins = "*")
public class SalaryInformationController {

    private final SalaryInformationService salaryInformationService;

    @PostMapping

    public ResponseEntity<SalaryInformationResponse> createSalaryInformation(
            @RequestBody SalaryInformationDTO salaryInformationDTO) {
        return new ResponseEntity<>(
                salaryInformationService.addSalaryInformation(salaryInformationDTO),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")

    public ResponseEntity<SalaryInformationResponse> updateSalaryInformation(
            @PathVariable String id,
            @RequestBody SalaryInformationDTO salaryInformationDTO) {
        return ResponseEntity.ok(
                salaryInformationService.updateSalaryInformation(salaryInformationDTO, id)
        );
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteSalaryInformation(@PathVariable String id) {
        salaryInformationService.deleteSalaryInformation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")

    public ResponseEntity<SalaryInformationResponse> getSalaryInformationById(
            @PathVariable String id) {
        return ResponseEntity.ok(
                salaryInformationService.findSalaryInformationById(id)
        );
    }

    @GetMapping

    public ResponseEntity<List<SalaryInformationResponse>> getAllSalaryInformations() {
        return ResponseEntity.ok(
                salaryInformationService.findAllSalaryInformations()
        );
    }
}