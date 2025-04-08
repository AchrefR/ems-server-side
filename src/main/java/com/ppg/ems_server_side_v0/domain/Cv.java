package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Cv extends BaseEntity {

    @OneToOne(mappedBy = "resume")
    private Application application;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employeeId", referencedColumnName = "id", nullable = false)
    private User employee;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "documentId", referencedColumnName = "id", nullable = false)
    private Document document;


}
