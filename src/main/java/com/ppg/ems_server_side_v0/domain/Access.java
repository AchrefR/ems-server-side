package com.ppg.ems_server_side_v0.domain;

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
public class Access {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Generator")
    @SequenceGenerator(name = "Generator", sequenceName = "Generator", allocationSize = 50)
    private String accessId;

    private String title;

    @ManyToMany(mappedBy = "accesses" , fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();

}
