package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Influencer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface InfluencerRepository extends JpaRepository<Influencer, String> {
    
    // Find by Instagram handle
    Optional<Influencer> findByInstagramHandle(String instagramHandle);
    
    // Find by niche
    Page<Influencer> findByNiche(Influencer.InfluencerNiche niche, Pageable pageable);
    
    // Find by tier
    Page<Influencer> findByTier(Influencer.InfluencerTier tier, Pageable pageable);
    
    // Find by collaboration status
    Page<Influencer> findByCollaborationStatus(Influencer.CollaborationStatus status, Pageable pageable);
    
    // Find by reputation
    Page<Influencer> findByReputation(Influencer.InfluencerReputation reputation, Pageable pageable);
    
    // Find by followers count range
    Page<Influencer> findByFollowersCountBetween(Long minFollowers, Long maxFollowers, Pageable pageable);
    
    // Find by engagement rate range
    Page<Influencer> findByEngagementRateBetween(BigDecimal minRate, BigDecimal maxRate, Pageable pageable);
    
    // Find by country
    Page<Influencer> findByCountryContainingIgnoreCase(String country, Pageable pageable);
    
    // Find by rate range
    Page<Influencer> findByRatePerPostBetween(BigDecimal minRate, BigDecimal maxRate, Pageable pageable);
    
    // Find verified influencers
    Page<Influencer> findByIsVerified(Boolean isVerified, Pageable pageable);
    
    // Custom queries for analytics
    @Query("SELECT COUNT(i) FROM Influencer i WHERE i.niche = :niche")
    Long getCountByNiche(@Param("niche") Influencer.InfluencerNiche niche);
    
    @Query("SELECT i.niche, COUNT(i) FROM Influencer i GROUP BY i.niche")
    List<Object[]> getInfluencersByNiche();
    
    @Query("SELECT i.tier, COUNT(i) FROM Influencer i GROUP BY i.tier")
    List<Object[]> getInfluencersByTier();
    
    @Query("SELECT i.collaborationStatus, COUNT(i) FROM Influencer i GROUP BY i.collaborationStatus")
    List<Object[]> getInfluencersByCollaborationStatus();
    
    @Query("SELECT i.reputation, COUNT(i) FROM Influencer i GROUP BY i.reputation")
    List<Object[]> getInfluencersByReputation();
    
    @Query("SELECT i.country, COUNT(i) FROM Influencer i GROUP BY i.country")
    List<Object[]> getInfluencersByCountry();
    
    @Query("SELECT AVG(i.followersCount) FROM Influencer i")
    Double getAverageFollowersCount();
    
    @Query("SELECT AVG(i.engagementRate) FROM Influencer i WHERE i.engagementRate IS NOT NULL")
    BigDecimal getAverageEngagementRate();
    
    @Query("SELECT AVG(i.ratePerPost) FROM Influencer i WHERE i.ratePerPost IS NOT NULL")
    BigDecimal getAverageRatePerPost();
    
    @Query("SELECT SUM(i.totalSpent) FROM Influencer i WHERE i.totalSpent IS NOT NULL")
    BigDecimal getTotalSpentOnInfluencers();
    
    @Query("SELECT COUNT(i) FROM Influencer i WHERE i.isVerified = true")
    Long getVerifiedInfluencersCount();
    
    // Search functionality
    @Query("SELECT i FROM Influencer i WHERE " +
           "LOWER(i.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.instagramHandle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.country) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.city) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Influencer> searchInfluencers(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    // Find top performers
    @Query("SELECT i FROM Influencer i WHERE i.engagementRate IS NOT NULL ORDER BY i.engagementRate DESC")
    Page<Influencer> findTopPerformersByEngagement(Pageable pageable);
    
    @Query("SELECT i FROM Influencer i ORDER BY i.followersCount DESC")
    Page<Influencer> findTopInfluencersByFollowers(Pageable pageable);
    
    // Find influencers needing follow-up
    @Query("SELECT i FROM Influencer i WHERE i.lastContactDate <= :cutoffDate AND i.collaborationStatus IN ('PROSPECT', 'CONTACTED', 'NEGOTIATING')")
    List<Influencer> getInfluencersNeedingFollowUp(@Param("cutoffDate") java.time.LocalDate cutoffDate);
}
