package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Role_Access;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Role_AccessRepository extends JpaRepository<Role_Access,String> {

    List<Role_Access> findRole_AccessesByRole_Role(String role);
}
