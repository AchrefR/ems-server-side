package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.PersonDTO;
import com.ppg.ems_server_side_v0.model.api.response.PersonResponse;
import com.ppg.ems_server_side_v0.service.core.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    @PostMapping("/")
    public ResponseEntity<PersonResponse> addPerson(@RequestBody PersonDTO personDTO) {
        return ResponseEntity.ok().body(this.personService.addPerson(personDTO));
    }

    @PutMapping("/updateById/{id}")
    public ResponseEntity<PersonResponse> updatePerson(@RequestBody PersonDTO personDTO, @PathVariable String id) {
        return ResponseEntity.ok(this.personService.updatePersonById(personDTO, id));
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deletePersonById(@PathVariable String id) {
        this.personService.deletePersonById(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<PersonResponse> findPersonById(@PathVariable String id) {
        return ResponseEntity.ok(this.personService.findPersonById(id));
    }

    @GetMapping("/findAll/")
    public ResponseEntity<List<PersonResponse>> findAllPersons() {
        return ResponseEntity.ok(this.personService.findAllPersons());
    }
}
