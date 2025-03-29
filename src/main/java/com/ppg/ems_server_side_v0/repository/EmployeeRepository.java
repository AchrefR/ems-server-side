package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,String> {
}
