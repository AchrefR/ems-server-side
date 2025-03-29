package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder,String> {
}
