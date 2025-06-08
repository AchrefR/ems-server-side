package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Attendance;
import com.ppg.ems_server_side_v0.domain.Employee;
import com.ppg.ems_server_side_v0.mapper.AttendanceMapper;
import com.ppg.ems_server_side_v0.model.api.request.AttendanceDTO;
import com.ppg.ems_server_side_v0.model.api.response.AttendanceResponse;
import com.ppg.ems_server_side_v0.repository.AttendanceRepository;
import com.ppg.ems_server_side_v0.repository.EmployeeRepository;
import com.ppg.ems_server_side_v0.service.core.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceMapper attendanceMapper;

    @Override
    public AttendanceResponse addAttendance(AttendanceDTO attendanceDTO) {
        Employee employee = employeeRepository.findById(attendanceDTO.relatedEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Attendance attendance = Attendance.builder()
                .attendanceDate(now.format(dateFormatter))
                .checkInTime(attendanceDTO.checkInTime())
                .checkOutTime(attendanceDTO.checkOutTime())
                .isAbsent(false)
                .relatedEmployee(employee)
                .build();

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return attendanceMapper.toAttendanceResponse(savedAttendance);
    }

    @Override
    public AttendanceResponse updateAttendance(AttendanceDTO attendanceDTO, String id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        if (attendanceDTO.relatedEmployeeId() != null) {
            Employee employee = employeeRepository.findById(attendanceDTO.relatedEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            attendance.setRelatedEmployee(employee);
        }

        if (attendanceDTO.checkInTime() != null) {
            attendance.setCheckInTime(attendanceDTO.checkInTime());
        }

        if (attendanceDTO.checkOutTime() != null) {
            attendance.setCheckOutTime(attendanceDTO.checkOutTime());
        }

        Attendance updatedAttendance = attendanceRepository.save(attendance);
        return attendanceMapper.toAttendanceResponse(updatedAttendance);
    }

    @Override
    public void deleteAttendance(String id) {
        if (!attendanceRepository.existsById(id)) {
            throw new RuntimeException("Attendance not found");
        }
        attendanceRepository.deleteById(id);
    }

    @Override
    public AttendanceResponse findAttendanceById(String id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));
        return attendanceMapper.toAttendanceResponse(attendance);
    }

    @Override
    public List<AttendanceResponse> findAllAttendances() {
        List<Attendance> attendances = attendanceRepository.findAll();
        return attendanceMapper.toAttendanceReponseList(attendances);
    }

    @Override
    public List<AttendanceResponse> findAttendancesByEmployeeId(String employeeId) {
        List<Attendance> attendances = attendanceRepository.findAll(employeeId).orElseThrow(() -> new RuntimeException("attendance is not found") );
        return attendanceMapper.toAttendanceReponseList(attendances);
    }
}