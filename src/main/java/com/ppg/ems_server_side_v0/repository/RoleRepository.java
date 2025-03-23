package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Role;
import com.ppg.ems_server_side_v0.domain.enums.DefaultRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findRoleByRole(String role);
}
