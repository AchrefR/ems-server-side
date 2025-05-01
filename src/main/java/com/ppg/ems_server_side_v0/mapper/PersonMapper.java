package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Person;
import com.ppg.ems_server_side_v0.model.api.response.PersonResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonMapper {

    public PersonResponse toPersonResponse(Person person) {

        return new PersonResponse(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getBirthDate(),
                person.getPhoneNumber(),
                person.getPersonType(),
                person.getAddress().getStreetName(),
                person.getAddress().getZipCode(),
                person.getAddress().getState(),
                person.getAddress().getTown()
        );
    }

    public List<PersonResponse> toPersonReponseList(List<Person> persons) {

        List<PersonResponse> personResponses = new ArrayList<>();
        persons.forEach(person -> {
            personResponses.add(toPersonResponse(person));
        });
        return personResponses;
    }

}
