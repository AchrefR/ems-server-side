package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
public class Contract extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "documentId", referencedColumnName = "id", nullable = false)
    private Document document;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employeeId", referencedColumnName = "id", nullable = false)
    private Employee employee;
}
