package com.ppg.ems_server_side_v0.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Address {

    @Id
    private String addressId;

    private String streetName;

    private String getStreetName;

    private String zipCode;

    private String state;

    private String town;

    @OneToOne(mappedBy = "address",fetch = FetchType.LAZY)
    private Person person;

}
