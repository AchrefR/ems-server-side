package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Address;
import com.ppg.ems_server_side_v0.domain.Application;
import com.ppg.ems_server_side_v0.domain.JobPost;
import com.ppg.ems_server_side_v0.domain.Person;
import com.ppg.ems_server_side_v0.mapper.AddressMapper;
import com.ppg.ems_server_side_v0.mapper.ApplicationMapper;
import com.ppg.ems_server_side_v0.model.api.request.AddressDTO;
import com.ppg.ems_server_side_v0.model.api.request.ApplicationDTO;
import com.ppg.ems_server_side_v0.model.api.response.AddressResponse;
import com.ppg.ems_server_side_v0.model.api.response.ApplicationResponse;
import com.ppg.ems_server_side_v0.model.api.response.PersonResponse;
import com.ppg.ems_server_side_v0.repository.AddressRepository;
import com.ppg.ems_server_side_v0.repository.ApplicationRepository;
import com.ppg.ems_server_side_v0.repository.JobPostRepository;
import com.ppg.ems_server_side_v0.repository.PersonRepository;
import com.ppg.ems_server_side_v0.service.core.AddressService;
import com.ppg.ems_server_side_v0.service.core.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationMapper applicationMapper;

    private final ApplicationRepository applicationRepository;

    private final PersonRepository personRepository;

    private final JobPostRepository jobPostRepository;

    private final AddressRepository addressRepository;

    @Override
    public ApplicationResponse addApplication(ApplicationDTO applicationDTO) {

        JobPost jobPost = this.jobPostRepository.findById(applicationDTO.jobPostId()).orElseThrow(() -> new RuntimeException("job post is not found"));

        Address address = this.addressRepository.save(Address.builder().
                streetName(applicationDTO.appliedBy().addressDTO().streetName()).
                zipCode(applicationDTO.appliedBy().addressDTO().zipCode()).
                state(applicationDTO.appliedBy().addressDTO().state()).
                town(applicationDTO.appliedBy().addressDTO().town())
                .build());

        Person appliedBy = this.personRepository.save(Person.builder().
                firstName(applicationDTO.appliedBy().firstName()).
                lastName(applicationDTO.appliedBy().lastName()).
                birthDate(applicationDTO.appliedBy().birthDate()).
                phoneNumber(applicationDTO.appliedBy().phoneNumber()).
                personType(applicationDTO.appliedBy().personType()).
                address(address).
                build());

        Application application = this.applicationRepository.save(Application.builder().
                title(applicationDTO.title()).
                description(applicationDTO.description()).
                appliedDate(applicationDTO.appliedDate()).
                jobPost(jobPost).
                appliedBy(appliedBy).
                build());
        return this.applicationMapper.toApplicationResponse(application);
    }

    @Override
    public ApplicationResponse updateApplication(ApplicationDTO applicationDTO, String id) {
        return null;
    }

    @Override
    public ApplicationResponse findApplicationById(String id) {

        Application application = this.applicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Application is not found"));
        return this.applicationMapper.toApplicationResponse(application);

    }

    @Override
    public void deleteApplication(String id) {
        this.applicationRepository.deleteById(id);
    }

    @Override
    public List<ApplicationResponse> findAllApplications() {
        List<Application> applications = this.applicationRepository.findAll();
        return this.applicationMapper.toApplicationResponseList(applications);
    }
}
