package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CV {

    @Id
    private String cvId;

    @OneToOne(mappedBy = "resume",fetch = FetchType.LAZY)
    Application application;


}
