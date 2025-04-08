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
    @JoinColumn(name = "personId", referencedColumnName = "id")
    private Person person;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salaryInformationId", referencedColumnName = "id")
    private SalaryInformation salaryInformation;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contractId", referencedColumnName = "id")
    private Contract contract;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "positionId", referencedColumnName = "id")
    private Position position;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departmentId", referencedColumnName = "id")
    private Department department;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamId", referencedColumnName = "id")
    private Team team;

    @OneToMany(mappedBy = "projectLeader")
    private List<Project> managedProjects;

    @OneToMany(mappedBy = "relatedEmployee")
    private List<PayCheck> payChecks;

    @OneToMany(mappedBy = "relatedEmployee")
    private List<Attendance> attendances;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "interviewerEmployee")
    private List<Interview> interviews;
}
