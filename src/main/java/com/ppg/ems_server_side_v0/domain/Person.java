package com.ppg.ems_server_side_v0.domain;

import com.ppg.ems_server_side_v0.domain.enums.PersonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class Person extends User {

    private String firstName;

    private String lastName;

    private String birthDate;

    private String phoneNumber;

    private PersonType personType;

    @OneToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name="addressId",referencedColumnName = "addressId",nullable = false)
    private Address address;

}
