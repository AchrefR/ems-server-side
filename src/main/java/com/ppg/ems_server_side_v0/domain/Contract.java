package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
public class Contract extends BaseEntity {

    private String contractId;

    private String contactNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentId", referencedColumnName = "id", nullable = false)
    private Document document;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "contract")
    private Employee employee;

    private static BigInteger incrementer = new BigInteger("0");

    public static String contractNumberGeneration()
    {
        incrementer = incrementer.add(BigInteger.valueOf(1));
        return String.format("%05d","CNTR-"+incrementer);
    }

}
