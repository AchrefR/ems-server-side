package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Department extends BaseEntity {

    private String departmentName;

    private String departmentType;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "department")
    private List<Employee> employees;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "department")
    private List<Document> documents;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "department")
    private List<Project> projects;





}
