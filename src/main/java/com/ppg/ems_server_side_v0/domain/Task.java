package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
public class Task extends BaseEntity {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String status;

    private String priority;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId", referencedColumnName = "id", nullable = false)
    private Project project;




}
