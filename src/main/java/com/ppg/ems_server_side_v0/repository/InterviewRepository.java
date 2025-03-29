package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview,String> {
}
