package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder

public class SalaryInformation extends BaseEntity {

    private BigDecimal salary;

    private double hourlyRate;

    @OneToOne(mappedBy = "salaryInformation",fetch = FetchType.EAGER)
    private Employee employee;

}
