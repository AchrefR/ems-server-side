package com.ppg.ems_server_side_v0.model.api.request;

public record AttendanceDTO(

        String checkInTime,

        String checkOutTime,

        String relatedEmployeeId
) {}
