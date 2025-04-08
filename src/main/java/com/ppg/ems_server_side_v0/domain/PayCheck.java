package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity

public class PayCheck  extends BaseEntity{

    private String payPeriod;

    private BigDecimal baseSalary;

    private int overTimeHours;

    private BigDecimal commission;

    private BigDecimal grossSalary;

    private BigDecimal taxDeduction;

    private BigDecimal socialSecurity;

    private BigDecimal healthInsurance;

    private BigDecimal retirementFund;

    private BigDecimal otherDeductions;

    private BigDecimal netSalary;

    private String paymentMethod;

    private String bankAccountNumber;

    private String bankName;

    private String bankBranch;

    private String bankSwiftCode;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "relatedEmployeeId", referencedColumnName = "id", nullable = false)
    private Employee relatedEmployee;


}
