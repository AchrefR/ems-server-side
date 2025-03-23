package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByRole_Role(String roleName);

    Optional<User> findByEmail(String email);
}
