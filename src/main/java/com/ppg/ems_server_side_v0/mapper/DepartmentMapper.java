package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Attendance;
import com.ppg.ems_server_side_v0.domain.Department;
import com.ppg.ems_server_side_v0.model.api.response.ContractResponse;
import com.ppg.ems_server_side_v0.model.api.response.DepartmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DepartmentMapper {

    public DepartmentResponse toDepartmentResponse(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getDepartmentName(),
                department.getDepartmentType()
        );
    }

    public List<DepartmentResponse> toDepartmentReponseList(List<Department> departmentList) {
        List<DepartmentResponse> departmentResponseList = new ArrayList<>();
        departmentList.forEach(department -> {
            departmentResponseList.add(toDepartmentResponse(department));
        });
        return departmentResponseList;
    }
}
