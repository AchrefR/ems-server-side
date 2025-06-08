package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,String> {
     @Query("SELECT a FROM Attendance a WHERE a.relatedEmployee.id = :employeeId")
     Optional<List<Attendance>> findAll(@Param("employeeId") String employeeId);

}
