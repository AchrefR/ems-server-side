package com.ppg.ems_server_side_v0.model.api.response;

public record AttendanceResponse(

        String attendanceId,

        String attendanceDate,

        String checkInTime,

        String checkOutTime,

        String employeeId

) {}
