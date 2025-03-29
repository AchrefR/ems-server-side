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
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Team extends BaseEntity {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "team")
    private List<Project> projects;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "team")
    private List<Employee> employees;


}
