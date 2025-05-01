package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Application;
import com.ppg.ems_server_side_v0.domain.Employee;
import com.ppg.ems_server_side_v0.domain.Interview;
import com.ppg.ems_server_side_v0.mapper.InterviewMapper;
import com.ppg.ems_server_side_v0.model.api.request.InterviewDTO;
import com.ppg.ems_server_side_v0.model.api.response.InterviewResponse;
import com.ppg.ems_server_side_v0.repository.ApplicationRepository;
import com.ppg.ems_server_side_v0.repository.EmployeeRepository;
import com.ppg.ems_server_side_v0.repository.InterviewRepository;
import com.ppg.ems_server_side_v0.service.core.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;

    private final InterviewMapper interviewMapper;

    private final ApplicationRepository applicationRepository;

    private final EmployeeRepository employeeRepository;

    @Override
    public InterviewResponse addInterview(InterviewDTO interviewDTO) {

        Application application = this.applicationRepository.findById(interviewDTO.applicationId()).orElseThrow(() -> new RuntimeException("Application is not found"));
        Employee employee = this.employeeRepository.findById(interviewDTO.interviewer()).orElseThrow(() -> new RuntimeException("employee is not found"));

        Interview interview = this.interviewRepository.save(
                Interview.builder().
                        date(interviewDTO.date()).
                        relatedApplication(application).
                        interviewerEmployee(employee)
                        .build()
        );

        return this.interviewMapper.toInterviewResponse(interview);
    }

    @Override
    public InterviewResponse updateInterviewById(InterviewResponse interviewResponse, String id) {

        Interview interview = this.interviewRepository.findById(id).orElseThrow(() -> new RuntimeException("interview is not found"));

        interview.setDate(interviewResponse.date());
        Interview interviewAfterChange = this.interviewRepository.save(interview);

        return this.interviewMapper.toInterviewResponse(interviewAfterChange);
    }

    @Override
    public void deleteInterviewById(String id) {

        this.interviewRepository.deleteById(id);
    }

    @Override
    public InterviewResponse findInterviewById(String id) {

        Interview interview = this.interviewRepository.findById(id).orElseThrow(() -> new RuntimeException("interview is not found "));

        return this.interviewMapper.toInterviewResponse(interview);
    }

    @Override
    public List<InterviewResponse> findAllInterviews() {

        List<Interview> interviews = this.interviewRepository.findAll();

        return this.interviewMapper.toInterviewResponseList(interviews);
    }
}
