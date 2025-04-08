package com.ppg.ems_server_side_v0.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessRepository extends JpaRepository<com.ppg.ems_server_side_v0.domain.Access, String> {
}
