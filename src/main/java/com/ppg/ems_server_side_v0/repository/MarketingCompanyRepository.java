package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.MarketingCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MarketingCompanyRepository extends JpaRepository<MarketingCompany, String> {
    
    // Find by industry
    Page<MarketingCompany> findByIndustryContainingIgnoreCase(String industry, Pageable pageable);
    
    // Find by company name
    Page<MarketingCompany> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);
    
    // Find by partnership status
    Page<MarketingCompany> findByPartnershipStatus(MarketingCompany.PartnershipStatus status, Pageable pageable);
    
    // Find by rating
    Page<MarketingCompany> findByRating(MarketingCompany.CompanyRating rating, Pageable pageable);
    
    // Find by company size
    Page<MarketingCompany> findByCompanySize(MarketingCompany.CompanySize size, Pageable pageable);
    
    // Find by country
    Page<MarketingCompany> findByCountryContainingIgnoreCase(String country, Pageable pageable);
    
    // Find by city
    Page<MarketingCompany> findByCityContainingIgnoreCase(String city, Pageable pageable);
    
    // Find by annual revenue range
    Page<MarketingCompany> findByAnnualRevenueBetween(BigDecimal minRevenue, BigDecimal maxRevenue, Pageable pageable);
    
    // Custom queries for analytics
    @Query("SELECT COUNT(m) FROM MarketingCompany m WHERE m.partnershipStatus = :status")
    Long getCountByPartnershipStatus(@Param("status") MarketingCompany.PartnershipStatus status);
    
    @Query("SELECT m.industry, COUNT(m) FROM MarketingCompany m GROUP BY m.industry")
    List<Object[]> getCompaniesByIndustry();
    
    @Query("SELECT m.rating, COUNT(m) FROM MarketingCompany m GROUP BY m.rating")
    List<Object[]> getCompaniesByRating();
    
    @Query("SELECT m.companySize, COUNT(m) FROM MarketingCompany m GROUP BY m.companySize")
    List<Object[]> getCompaniesBySize();
    
    @Query("SELECT m.country, COUNT(m) FROM MarketingCompany m GROUP BY m.country")
    List<Object[]> getCompaniesByCountry();
    
    @Query("SELECT AVG(m.annualRevenue) FROM MarketingCompany m WHERE m.annualRevenue IS NOT NULL")
    BigDecimal getAverageAnnualRevenue();
    
    @Query("SELECT SUM(m.potentialDealValue) FROM MarketingCompany m WHERE m.potentialDealValue IS NOT NULL")
    BigDecimal getTotalPotentialDealValue();
    
    @Query("SELECT AVG(m.satisfactionScore) FROM MarketingCompany m WHERE m.satisfactionScore IS NOT NULL")
    BigDecimal getAverageSatisfactionScore();
    
    // Search functionality
    @Query("SELECT m FROM MarketingCompany m WHERE " +
           "LOWER(m.companyName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.industry) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.contactPersonName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.country) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.city) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<MarketingCompany> searchMarketingCompanies(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    // Find companies needing follow-up
    @Query("SELECT m FROM MarketingCompany m WHERE m.nextContactDate <= CURRENT_DATE AND m.partnershipStatus IN ('PROSPECT', 'CONTACTED', 'NEGOTIATING')")
    List<MarketingCompany> getCompaniesNeedingFollowUp();
}
