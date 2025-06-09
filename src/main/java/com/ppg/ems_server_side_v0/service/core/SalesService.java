package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.dto.*;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SalesService {
    
    // Sales Dashboard
    SalesDashboardResponse getSalesDashboard();
    Map<String, Object> getSalesStatistics();
    Map<String, Object> getMarketingStatistics();
    Map<String, Object> getInfluencerStatistics();
    
    // Sales Records CRUD
    SalesRecordResponse createSalesRecord(SalesRecordRequest request);
    SalesRecordResponse updateSalesRecord(String id, SalesRecordRequest request);
    SalesRecordResponse getSalesRecordById(String id);
    Page<SalesRecordResponse> getAllSalesRecords(int page, int size, String sortBy, String sortDirection);
    void deleteSalesRecord(String id);
    
    // Sales Records Advanced Operations
    Page<SalesRecordResponse> searchSalesRecords(String searchTerm, int page, int size);
    Page<SalesRecordResponse> getSalesRecordsByStatus(String status, int page, int size);
    Page<SalesRecordResponse> getSalesRecordsByEmployee(String employeeId, int page, int size);
    Page<SalesRecordResponse> getSalesRecordsByDateRange(LocalDate startDate, LocalDate endDate, int page, int size);
    List<SalesRecordResponse> getOverdueFollowUps();
    
    // Marketing Companies CRUD
    MarketingCompanyResponse createMarketingCompany(MarketingCompanyRequest request);
    MarketingCompanyResponse updateMarketingCompany(String id, MarketingCompanyRequest request);
    MarketingCompanyResponse getMarketingCompanyById(String id);
    Page<MarketingCompanyResponse> getAllMarketingCompanies(int page, int size, String sortBy, String sortDirection);
    void deleteMarketingCompany(String id);
    
    // Marketing Companies Advanced Operations
    Page<MarketingCompanyResponse> searchMarketingCompanies(String searchTerm, int page, int size);
    Page<MarketingCompanyResponse> getMarketingCompaniesByIndustry(String industry, int page, int size);
    Page<MarketingCompanyResponse> getMarketingCompaniesByStatus(String status, int page, int size);
    List<MarketingCompanyResponse> getCompaniesNeedingFollowUp();
    
    // Influencers CRUD
    InfluencerResponse createInfluencer(InfluencerRequest request);
    InfluencerResponse updateInfluencer(String id, InfluencerRequest request);
    InfluencerResponse getInfluencerById(String id);
    Page<InfluencerResponse> getAllInfluencers(int page, int size, String sortBy, String sortDirection);
    void deleteInfluencer(String id);
    
    // Influencers Advanced Operations
    Page<InfluencerResponse> searchInfluencers(String searchTerm, int page, int size);
    Page<InfluencerResponse> getInfluencersByNiche(String niche, int page, int size);
    Page<InfluencerResponse> getInfluencersByTier(String tier, int page, int size);
    Page<InfluencerResponse> getTopInfluencersByEngagement(int page, int size);
    Page<InfluencerResponse> getTopInfluencersByFollowers(int page, int size);
    List<InfluencerResponse> getInfluencersNeedingFollowUp();
    
    // Analytics and Reports
    Map<String, Object> getSalesAnalytics(LocalDate startDate, LocalDate endDate);
    Map<String, Object> getMarketingAnalytics();
    Map<String, Object> getInfluencerAnalytics();
    
    // Bulk Operations
    List<SalesRecordResponse> bulkCreateSalesRecords(List<SalesRecordRequest> requests);
    List<MarketingCompanyResponse> bulkCreateMarketingCompanies(List<MarketingCompanyRequest> requests);
    List<InfluencerResponse> bulkCreateInfluencers(List<InfluencerRequest> requests);
}
