package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Interview;
import com.ppg.ems_server_side_v0.model.api.response.InterviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InterviewMapper {

    InterviewResponse toInterviewResponse(Interview interview) {
        return new InterviewResponse(
                interview.getId(),
                interview.getRelatedApplication().getId(),
                interview.getInterviewerEmployee().getId()
        );
    }

    List<InterviewResponse> toInterviewResponseList(List<Interview> interviews) {
        List<InterviewResponse> interviewsResponses = new ArrayList<>();
        interviews.forEach(interview -> {
            interviewsResponses.add(toInterviewResponse(interview));
        });
        return interviewsResponses;
    }
}
