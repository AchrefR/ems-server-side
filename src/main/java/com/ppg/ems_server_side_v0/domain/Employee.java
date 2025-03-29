package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Employee extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salaryInformationId", referencedColumnName = "id", nullable = false)
    private SalaryInformation salaryInformation;

    @OneToOne(fetch = FetchType.EAGER,mappedBy = "employee")
    private Contract contract;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "positionId", referencedColumnName = "id", nullable = false)
    private Position position;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "departmentId", referencedColumnName = "id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "teamId", referencedColumnName = "id", nullable = false)
    private Team team;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "projectLeader")
    private List<Project> managedProjects;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "relatedEmployee")
    private List<PayCheck> payChecks;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "relatedEmployee")
    private List<Attendance> attendances;



}
