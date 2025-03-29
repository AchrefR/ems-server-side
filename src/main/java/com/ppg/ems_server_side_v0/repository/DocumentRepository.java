package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,String> {
}
