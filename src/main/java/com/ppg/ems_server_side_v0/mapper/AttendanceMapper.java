package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Attendance;
import com.ppg.ems_server_side_v0.model.api.response.AttendanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AttendanceMapper {

    public AttendanceResponse toAttendanceResponse(Attendance attendance) {
        return new AttendanceResponse(
                attendance.getId(),
                attendance.getAttendanceDate(),
                attendance.getCheckInTime(),
                attendance.getCheckOutTime(),
                attendance.getRelatedEmployee().getId()
        );
    }

    public List<AttendanceResponse> toAttendanceReponseList(List<Attendance> attendanceList) {
        List<AttendanceResponse> attendanceResponseList = new ArrayList<>();
        attendanceList.forEach(attendance -> {
            attendanceResponseList.add(toAttendanceResponse(attendance));
        });
        return attendanceResponseList;
    }
}
