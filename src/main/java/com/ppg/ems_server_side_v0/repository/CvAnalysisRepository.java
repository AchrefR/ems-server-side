package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.CvAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CvAnalysisRepository extends JpaRepository<CvAnalysis, String> {
    
    List<CvAnalysis> findByJobDescriptionIdOrderByOverallScoreDesc(String jobDescriptionId);
    
    @Query("SELECT ca FROM CvAnalysis ca WHERE ca.jobDescription.id = :jobDescriptionId ORDER BY ca.overallScore DESC")
    List<CvAnalysis> findTopCvsByJobDescription(@Param("jobDescriptionId") String jobDescriptionId);
    
    @Query("SELECT ca FROM CvAnalysis ca WHERE ca.jobDescription.id = :jobDescriptionId AND ca.overallScore >= :minScore ORDER BY ca.overallScore DESC")
    List<CvAnalysis> findCvsByJobDescriptionAndMinScore(@Param("jobDescriptionId") String jobDescriptionId, @Param("minScore") Double minScore);
}
