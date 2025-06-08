package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.AttendanceDTO;
import com.ppg.ems_server_side_v0.model.api.response.AttendanceResponse;

import java.util.List;

public interface AttendanceService {

    AttendanceResponse addAttendance(AttendanceDTO attendanceDTO);

    AttendanceResponse updateAttendance(AttendanceDTO attendanceDTO, String id);

    void deleteAttendance(String id);

    AttendanceResponse findAttendanceById(String id);

    List<AttendanceResponse> findAllAttendances();
    
    List<AttendanceResponse> findAttendancesByEmployeeId(String employeeId);
}