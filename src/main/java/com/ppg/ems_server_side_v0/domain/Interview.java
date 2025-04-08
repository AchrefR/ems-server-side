package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Interview extends BaseEntity{

    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relatedApplicationId", referencedColumnName = "id", nullable = false)
    private Application relatedApplication;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "interviewerEmployeeId", referencedColumnName = "id", nullable = false)
    private Employee interviewerEmployee;
}
