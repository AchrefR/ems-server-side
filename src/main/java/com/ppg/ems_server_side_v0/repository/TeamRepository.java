package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,String> {
}
