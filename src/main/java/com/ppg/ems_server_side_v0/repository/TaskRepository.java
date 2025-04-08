package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.SalaryInformation;
import com.ppg.ems_server_side_v0.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task,String> {
}
