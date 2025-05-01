package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Address;
import com.ppg.ems_server_side_v0.domain.Person;
import com.ppg.ems_server_side_v0.mapper.PersonMapper;
import com.ppg.ems_server_side_v0.model.api.request.PersonDTO;
import com.ppg.ems_server_side_v0.model.api.response.PersonResponse;
import com.ppg.ems_server_side_v0.repository.AddressRepository;
import com.ppg.ems_server_side_v0.repository.PersonRepository;
import com.ppg.ems_server_side_v0.service.core.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final AddressRepository addressRepository;

    private final PersonMapper personMapper;

    @Override
    public PersonResponse addPerson(PersonDTO personDTO) {

        Address address = this.addressRepository.save(Address.builder().
                streetName(personDTO.addressDTO().streetName()).
                zipCode(personDTO.addressDTO().zipCode()).
                state(personDTO.addressDTO().state()).
                town(personDTO.addressDTO().town())
                .build());

        Person person = this.personRepository.save(Person.builder().
                firstName(personDTO.firstName()).
                lastName(personDTO.lastName()).
                birthDate(personDTO.birthDate()).
                phoneNumber(personDTO.phoneNumber()).
                personType(personDTO.personType()).
                address(address).
                build());

        return this.personMapper.toPersonResponse(person);
    }

    @Override
    public PersonResponse updatePersonById(PersonDTO personDTO, String id) {

        Person person = this.personRepository.findById(id).orElseThrow(()->new RuntimeException("person is not found"));


        return null;
    }

    @Override
    public void deletePersonById(String id) {

    }

    @Override
    public PersonResponse findPersonById(String id) {
        return null;
    }

    @Override
    public List<PersonResponse> findAllPersons() {
        return List.of();
    }
}
