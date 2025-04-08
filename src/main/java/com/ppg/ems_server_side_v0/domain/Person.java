package com.ppg.ems_server_side_v0.domain;

import com.ppg.ems_server_side_v0.domain.enums.PersonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class Person extends BaseEntity {

    private String firstName;

    private String lastName;

    private String birthDate;

    private String phoneNumber;

    private String personType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "addressId", referencedColumnName = "id", nullable = false)
    private Address address;

    @OneToOne( mappedBy = "person")
    private User user;

    @OneToMany( mappedBy = "appliedBy")
    private List<Application> applications;

    @OneToOne( mappedBy = "person")
    private Employee employee;

}
