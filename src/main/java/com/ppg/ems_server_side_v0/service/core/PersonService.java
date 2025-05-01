package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.PersonDTO;
import com.ppg.ems_server_side_v0.model.api.response.PersonResponse;

import java.util.List;

public interface PersonService {

    PersonResponse addPerson(PersonDTO personDTO);

    PersonResponse updatePersonById(PersonDTO personDTO, String id);

    void deletePersonById(String id);

    PersonResponse findPersonById(String id);

    List<PersonResponse> findAllPersons();

}
