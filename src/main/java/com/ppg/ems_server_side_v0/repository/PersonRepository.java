package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person,String> {
}
