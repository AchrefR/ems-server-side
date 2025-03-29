package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance,String> {
}
