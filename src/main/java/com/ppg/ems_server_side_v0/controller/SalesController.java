package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.dto.*;
import com.ppg.ems_server_side_v0.service.core.SalesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class SalesController {

    private final SalesService salesService;



    // Dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<SalesDashboardResponse> getSalesDashboard() {
        try {
            SalesDashboardResponse dashboard = salesService.getSalesDashboard();
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            log.error("Error fetching sales dashboard: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getSalesStatistics() {
        try {
            Map<String, Object> statistics = salesService.getSalesStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            log.error("Error fetching sales statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statistics/marketing")
    public ResponseEntity<Map<String, Object>> getMarketingStatistics() {
        try {
            Map<String, Object> statistics = salesService.getMarketingStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            log.error("Error fetching marketing statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statistics/influencers")
    public ResponseEntity<Map<String, Object>> getInfluencerStatistics() {
        try {
            Map<String, Object> statistics = salesService.getInfluencerStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            log.error("Error fetching influencer statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Sales Records CRUD
    @PostMapping("/records")
    public ResponseEntity<SalesRecordResponse> createSalesRecord(@RequestBody SalesRecordRequest request) {
        try {
            SalesRecordResponse response = salesService.createSalesRecord(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating sales record: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/records")
    public ResponseEntity<Page<SalesRecordResponse>> getAllSalesRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Page<SalesRecordResponse> salesRecords = salesService.getAllSalesRecords(page, size, sortBy, sortDirection);
            return ResponseEntity.ok(salesRecords);
        } catch (Exception e) {
            log.error("Error fetching sales records: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/records/{id}")
    public ResponseEntity<SalesRecordResponse> getSalesRecordById(@PathVariable String id) {
        try {
            SalesRecordResponse response = salesService.getSalesRecordById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching sales record: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/records/{id}")
    public ResponseEntity<SalesRecordResponse> updateSalesRecord(
            @PathVariable String id, 
            @RequestBody SalesRecordRequest request) {
        try {
            SalesRecordResponse response = salesService.updateSalesRecord(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating sales record: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/records/{id}")
    public ResponseEntity<Void> deleteSalesRecord(@PathVariable String id) {
        try {
            salesService.deleteSalesRecord(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting sales record: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Sales Records Advanced Operations
    @GetMapping("/records/search")
    public ResponseEntity<Page<SalesRecordResponse>> searchSalesRecords(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<SalesRecordResponse> salesRecords = salesService.searchSalesRecords(searchTerm, page, size);
            return ResponseEntity.ok(salesRecords);
        } catch (Exception e) {
            log.error("Error searching sales records: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/records/status/{status}")
    public ResponseEntity<Page<SalesRecordResponse>> getSalesRecordsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<SalesRecordResponse> salesRecords = salesService.getSalesRecordsByStatus(status, page, size);
            return ResponseEntity.ok(salesRecords);
        } catch (Exception e) {
            log.error("Error fetching sales records by status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/records/employee/{employeeId}")
    public ResponseEntity<Page<SalesRecordResponse>> getSalesRecordsByEmployee(
            @PathVariable String employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<SalesRecordResponse> salesRecords = salesService.getSalesRecordsByEmployee(employeeId, page, size);
            return ResponseEntity.ok(salesRecords);
        } catch (Exception e) {
            log.error("Error fetching sales records by employee: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/records/date-range")
    public ResponseEntity<Page<SalesRecordResponse>> getSalesRecordsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<SalesRecordResponse> salesRecords = salesService.getSalesRecordsByDateRange(startDate, endDate, page, size);
            return ResponseEntity.ok(salesRecords);
        } catch (Exception e) {
            log.error("Error fetching sales records by date range: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/records/overdue-followups")
    public ResponseEntity<List<SalesRecordResponse>> getOverdueFollowUps() {
        try {
            List<SalesRecordResponse> overdueRecords = salesService.getOverdueFollowUps();
            return ResponseEntity.ok(overdueRecords);
        } catch (Exception e) {
            log.error("Error fetching overdue follow-ups: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Marketing Companies CRUD
    @PostMapping("/companies")
    public ResponseEntity<MarketingCompanyResponse> createMarketingCompany(@RequestBody MarketingCompanyRequest request) {
        try {
            MarketingCompanyResponse response = salesService.createMarketingCompany(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating marketing company: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/companies")
    public ResponseEntity<Page<MarketingCompanyResponse>> getAllMarketingCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "companyName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<MarketingCompanyResponse> companies = salesService.getAllMarketingCompanies(page, size, sortBy, sortDirection);
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            log.error("Error fetching marketing companies: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<MarketingCompanyResponse> getMarketingCompanyById(@PathVariable String id) {
        try {
            MarketingCompanyResponse response = salesService.getMarketingCompanyById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching marketing company: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<MarketingCompanyResponse> updateMarketingCompany(
            @PathVariable String id, 
            @RequestBody MarketingCompanyRequest request) {
        try {
            MarketingCompanyResponse response = salesService.updateMarketingCompany(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating marketing company: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteMarketingCompany(@PathVariable String id) {
        try {
            salesService.deleteMarketingCompany(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting marketing company: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Marketing Companies Advanced Operations
    @GetMapping("/companies/search")
    public ResponseEntity<Page<MarketingCompanyResponse>> searchMarketingCompanies(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<MarketingCompanyResponse> companies = salesService.searchMarketingCompanies(searchTerm, page, size);
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            log.error("Error searching marketing companies: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/companies/industry/{industry}")
    public ResponseEntity<Page<MarketingCompanyResponse>> getMarketingCompaniesByIndustry(
            @PathVariable String industry,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<MarketingCompanyResponse> companies = salesService.getMarketingCompaniesByIndustry(industry, page, size);
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            log.error("Error fetching marketing companies by industry: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/companies/status/{status}")
    public ResponseEntity<Page<MarketingCompanyResponse>> getMarketingCompaniesByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<MarketingCompanyResponse> companies = salesService.getMarketingCompaniesByStatus(status, page, size);
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            log.error("Error fetching marketing companies by status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/companies/follow-up")
    public ResponseEntity<List<MarketingCompanyResponse>> getCompaniesNeedingFollowUp() {
        try {
            List<MarketingCompanyResponse> companies = salesService.getCompaniesNeedingFollowUp();
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            log.error("Error fetching companies needing follow-up: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Influencers CRUD
    @PostMapping("/influencers")
    public ResponseEntity<InfluencerResponse> createInfluencer(@RequestBody InfluencerRequest request) {
        try {
            InfluencerResponse response = salesService.createInfluencer(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating influencer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/influencers")
    public ResponseEntity<Page<InfluencerResponse>> getAllInfluencers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<InfluencerResponse> influencers = salesService.getAllInfluencers(page, size, sortBy, sortDirection);
            return ResponseEntity.ok(influencers);
        } catch (Exception e) {
            log.error("Error fetching influencers: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/influencers/{id}")
    public ResponseEntity<InfluencerResponse> getInfluencerById(@PathVariable String id) {
        try {
            InfluencerResponse response = salesService.getInfluencerById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching influencer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/influencers/{id}")
    public ResponseEntity<InfluencerResponse> updateInfluencer(
            @PathVariable String id,
            @RequestBody InfluencerRequest request) {
        try {
            InfluencerResponse response = salesService.updateInfluencer(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating influencer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/influencers/{id}")
    public ResponseEntity<Void> deleteInfluencer(@PathVariable String id) {
        try {
            salesService.deleteInfluencer(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting influencer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Influencers Advanced Operations
    @GetMapping("/influencers/search")
    public ResponseEntity<Page<InfluencerResponse>> searchInfluencers(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<InfluencerResponse> influencers = salesService.searchInfluencers(searchTerm, page, size);
            return ResponseEntity.ok(influencers);
        } catch (Exception e) {
            log.error("Error searching influencers: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/influencers/niche/{niche}")
    public ResponseEntity<Page<InfluencerResponse>> getInfluencersByNiche(
            @PathVariable String niche,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<InfluencerResponse> influencers = salesService.getInfluencersByNiche(niche, page, size);
            return ResponseEntity.ok(influencers);
        } catch (Exception e) {
            log.error("Error fetching influencers by niche: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/influencers/tier/{tier}")
    public ResponseEntity<Page<InfluencerResponse>> getInfluencersByTier(
            @PathVariable String tier,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<InfluencerResponse> influencers = salesService.getInfluencersByTier(tier, page, size);
            return ResponseEntity.ok(influencers);
        } catch (Exception e) {
            log.error("Error fetching influencers by tier: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/influencers/top/engagement")
    public ResponseEntity<Page<InfluencerResponse>> getTopInfluencersByEngagement(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<InfluencerResponse> influencers = salesService.getTopInfluencersByEngagement(page, size);
            return ResponseEntity.ok(influencers);
        } catch (Exception e) {
            log.error("Error fetching top influencers by engagement: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/influencers/top/followers")
    public ResponseEntity<Page<InfluencerResponse>> getTopInfluencersByFollowers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<InfluencerResponse> influencers = salesService.getTopInfluencersByFollowers(page, size);
            return ResponseEntity.ok(influencers);
        } catch (Exception e) {
            log.error("Error fetching top influencers by followers: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/influencers/follow-up")
    public ResponseEntity<List<InfluencerResponse>> getInfluencersNeedingFollowUp() {
        try {
            List<InfluencerResponse> influencers = salesService.getInfluencersNeedingFollowUp();
            return ResponseEntity.ok(influencers);
        } catch (Exception e) {
            log.error("Error fetching influencers needing follow-up: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
