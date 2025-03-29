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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role")
    private List<User> users = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Role_Access", joinColumns = {
            @JoinColumn(name = "roleId")
    }, inverseJoinColumns = {
            @JoinColumn(name = "accessId")
    })
    private List<Access> accesses = new ArrayList<>();


}
