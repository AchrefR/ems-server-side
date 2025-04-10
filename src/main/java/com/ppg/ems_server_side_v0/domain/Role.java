package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuperBuilder
@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Role extends BaseEntity {

    private String role;

    @OneToMany( mappedBy = "role")
    private List<User> users = new ArrayList<>();

    @OneToMany( mappedBy = "role")
    private List<Role_Access> role_accesses = new ArrayList<Role_Access>();


}
