package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Department;
import com.ppg.ems_server_side_v0.mapper.DepartmentMapper;
import com.ppg.ems_server_side_v0.model.api.request.DepartmentDTO;
import com.ppg.ems_server_side_v0.model.api.response.DepartmentResponse;
import com.ppg.ems_server_side_v0.repository.DepartmentRepository;
import com.ppg.ems_server_side_v0.service.core.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse addDepartment(DepartmentDTO departmentDTO) {
        Department department = Department.builder()
                .departmentName(departmentDTO.departmentName())
                .departmentType(departmentDTO.departmentType())
                .build();

        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDepartmentResponse(savedDepartment);
    }

    @Override
    public DepartmentResponse updateDepartment(DepartmentDTO departmentDTO, String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        if (departmentDTO.departmentName() != null) {
            department.setDepartmentName(departmentDTO.departmentName());
        }

        if (departmentDTO.departmentType() != null) {
            department.setDepartmentType(departmentDTO.departmentType());
        }

        Department updatedDepartment = departmentRepository.save(department);
        return departmentMapper.toDepartmentResponse(updatedDepartment);
    }

    @Override
    public void deleteDepartment(String id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found");
        }
        departmentRepository.deleteById(id);
    }

    @Override
    public DepartmentResponse findDepartmentById(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return departmentMapper.toDepartmentResponse(department);
    }

    @Override
    public List<DepartmentResponse> findAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departmentMapper.toDepartmentReponseList(departments);
    }
}