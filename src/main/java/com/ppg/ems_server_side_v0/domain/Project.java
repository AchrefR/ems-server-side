package com.ppg.ems_server_side_v0.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Project extends BaseEntity {

    private String title;

    private String description;

    private String deadline;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "departmentId", referencedColumnName = "id", nullable = false)
    private Department department;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project")
    private List<Task> tasks;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "teamId", referencedColumnName = "id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "projectLeaderId", referencedColumnName = "id", nullable = false)
    private Employee projectLeader;


}
