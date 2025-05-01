package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.ApplicationDTO;
import com.ppg.ems_server_side_v0.model.api.response.ApplicationResponse;

import java.util.List;

public interface ApplicationService {

    ApplicationResponse addApplication(ApplicationDTO applicationDTO);

    ApplicationResponse updateApplication(ApplicationDTO applicationDTO, String id);

    void deleteApplication(String id);

    ApplicationResponse findApplicationById(String id);

    List<ApplicationResponse> findAllApplications();


}
