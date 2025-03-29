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

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Application extends BaseEntity {

    private String title;

    private String description;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "resumeId", referencedColumnName = "id", nullable = false)
    private Cv resume;

    private LocalDateTime appliedDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "jobPostId", referencedColumnName = "id", nullable = false)
    private JobPost jobPost;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "appliedByPersonId", referencedColumnName = "id", nullable = false)
    private Person appliedBy;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "relatedApplication")
    private List<Interview> interview;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "interviewerEmployeeId", referencedColumnName = "id", nullable = false)
    private Employee interviewerEmployee;
}
