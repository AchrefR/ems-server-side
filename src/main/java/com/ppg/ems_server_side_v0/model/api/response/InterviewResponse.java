package com.ppg.ems_server_side_v0.model.api.response;

import com.ppg.ems_server_side_v0.model.api.request.EmployeeDTO;

public record InterviewResponse(

        String interviewId,

        String date,

        String applicationId,

        String interviewerId

        ) {}
