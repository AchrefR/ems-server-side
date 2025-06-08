package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.DepartmentDTO;
import com.ppg.ems_server_side_v0.model.api.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse addDepartment(DepartmentDTO departmentDTO);

    DepartmentResponse updateDepartment(DepartmentDTO departmentDTO, String id);

    void deleteDepartment(String id);

    DepartmentResponse findDepartmentById(String id);

    List<DepartmentResponse> findAllDepartments();
}