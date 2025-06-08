package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.EmployeeDTO;
import com.ppg.ems_server_side_v0.model.api.response.EmployeeResponse;
import com.ppg.ems_server_side_v0.service.core.EmployeeService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor

public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping

    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return new ResponseEntity<>(employeeService.addEmployee(employeeDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")

    public ResponseEntity<EmployeeResponse> updateEmployee(
            @RequestBody EmployeeDTO employeeDTO,
            @PathVariable String id) {
        return ResponseEntity.ok(employeeService.updateEmployeeById(employeeDTO, id));
    }

    @DeleteMapping("/{id}")


    public ResponseEntity<EmployeeResponse> deleteEmployee(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.findEmployeeById(id));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAllEmployees());
    }
}