package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
public class Attendance extends BaseEntity {

    private String checkInTime;

    private String checkOutTime;

    private BigDecimal workHours;

    private boolean isAbsent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="relatedEmployeeId",referencedColumnName = "id")
    private Employee relatedEmployee;


}
