package com.ppg.ems_server_side_v0.domain;

import com.ppg.ems_server_side_v0.domain.enums.DefaultRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Generator")
    @SequenceGenerator(name = "Generator",sequenceName = "Generator" ,allocationSize = 50)
    private String id;

    private DefaultRole role;

}
