package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    @Id
    private String applicationId;

    private String title;

    private String description;

    @OneToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name="resumeId",referencedColumnName = "cvId",nullable = false)
    private CV resume;

    private LocalDateTime appliedDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="jobPostId",referencedColumnName = "jobPostId",nullable = false)
    private JobPost jobPost;
}
