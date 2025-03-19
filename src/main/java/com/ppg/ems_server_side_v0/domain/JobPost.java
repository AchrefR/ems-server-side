package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class JobPost {

    @Id
    private String jobPostId;

    private String title;

    private String description;

    @OneToMany(mappedBy = "jobPost")
    private List<Application> application = new ArrayList<Application>();

}
