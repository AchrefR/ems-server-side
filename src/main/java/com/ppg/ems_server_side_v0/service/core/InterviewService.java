package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.InterviewDTO;
import com.ppg.ems_server_side_v0.model.api.response.InterviewResponse;

import java.util.List;

public interface InterviewService {

    InterviewResponse addInterview(InterviewDTO interviewDTO);

    InterviewResponse updateInterviewById(InterviewDTO interviewDTO, String id);

    void deleteInterviewById(String id);

    InterviewResponse findInterviewById(String id);

    List<InterviewResponse> findAllInterviews();


}
