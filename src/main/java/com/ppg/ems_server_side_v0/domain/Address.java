package com.ppg.ems_server_side_v0.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data

public class Address extends BaseEntity {

    private String streetName;

    private String zipCode;

    private String state;

    private String town;

    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
    private Person person;

}
