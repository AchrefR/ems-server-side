package com.ppg.ems_server_side_v0.domain;

import com.ppg.ems_server_side_v0.domain.enums.DefaultRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Generator")
    @SequenceGenerator(name = "Generator",sequenceName = "Generator" ,allocationSize = 50)
    private String roleId;

    private DefaultRole role;

    @OneToMany(fetch = FetchType.LAZY , mappedBy = "role", cascade = CascadeType.ALL)
    List<User> users = new ArrayList<>();;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Role_Access",joinColumns = {
            @JoinColumn(referencedColumnName = "roleId")
    },inverseJoinColumns = {
            @JoinColumn(referencedColumnName = "accessId")
    })
    List<Access> accesses =  new ArrayList<>();


}
