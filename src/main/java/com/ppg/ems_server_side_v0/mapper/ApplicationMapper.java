package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Application;
import com.ppg.ems_server_side_v0.model.api.response.ApplicationResponse;
import com.ppg.ems_server_side_v0.model.api.response.JobPostResponse;
import com.ppg.ems_server_side_v0.model.api.response.PersonResponse;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationMapper {

    public ApplicationResponse toApplicationResponse(Application application) {
        return new ApplicationResponse(
                application.getId(),
                application.getDescription(),
                application.getAppliedDate(),
                application.getJobPost().getId(),
                application.getAppliedBy().getId()
        );
    }

    public List<ApplicationResponse> toApplicationResponseList(List<Application> applications) {
        List<ApplicationResponse> applicationResponses = new ArrayList<>();
        applications.forEach(application -> {
            applicationResponses.add(toApplicationResponse(application));
        });
        return applicationResponses;
    }
}
