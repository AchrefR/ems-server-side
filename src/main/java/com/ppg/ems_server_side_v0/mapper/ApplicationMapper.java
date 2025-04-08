package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Application;
import com.ppg.ems_server_side_v0.model.api.response.ApplicationResponse;
import com.ppg.ems_server_side_v0.model.api.response.JobPostResponse;
import com.ppg.ems_server_side_v0.model.api.response.PersonResponse;

import java.util.ArrayList;
import java.util.List;

public class ApplicationMapper {

    ApplicationResponse toApplicationResponse(Application application) {
        return new ApplicationResponse(
                application.getId(),
                application.getDescription(),
                application.getAppliedDate(),
                new JobPostResponse(
                        application.getJobPost().getId(),
                        application.getJobPost().getTitle(),
                        application.getJobPost().getDescription()
                ),
                new PersonResponse(
                        application.getAppliedBy().getId(),
                        application.getAppliedBy().getFirstName(),
                        application.getAppliedBy().getLastName(),
                        application.getAppliedBy().getBirthDate(),
                        application.getAppliedBy().getPhoneNumber(),
                        application.getAppliedBy().getPersonType(),
                        application.getAppliedBy().getAddress().getStreetName(),
                        application.getAppliedBy().getAddress().getZipCode(),
                        application.getAppliedBy().getAddress().getState(),
                        application.getAppliedBy().getAddress().getTown()
                )
        );
    }

    List<ApplicationResponse> toApplicationResponseList(List<Application> applications) {
        List<ApplicationResponse> applicationResponses = new ArrayList<>();
        applications.forEach(application -> {
            applicationResponses.add(toApplicationResponse(application));
        });
        return applicationResponses;
    }
}
