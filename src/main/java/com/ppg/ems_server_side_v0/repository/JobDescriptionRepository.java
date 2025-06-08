package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.JobDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobDescriptionRepository extends JpaRepository<JobDescription, String> {
    
    List<JobDescription> findByDepartmentOrderByCreatedAtDesc(String department);
    
    @Query("SELECT jd FROM JobDescription jd WHERE jd.title LIKE %:keyword% OR jd.description LIKE %:keyword%")
    List<JobDescription> findByKeyword(@Param("keyword") String keyword);
    
    List<JobDescription> findByCreatedByIdOrderByCreatedAtDesc(String userId);
}
