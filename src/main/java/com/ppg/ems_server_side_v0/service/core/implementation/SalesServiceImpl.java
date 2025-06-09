package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.*;
import com.ppg.ems_server_side_v0.dto.*;
import com.ppg.ems_server_side_v0.repository.*;
import com.ppg.ems_server_side_v0.service.core.SalesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SalesServiceImpl implements SalesService {

    private final SalesRecordRepository salesRecordRepository;
    private final MarketingCompanyRepository marketingCompanyRepository;
    private final InfluencerRepository influencerRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public SalesDashboardResponse getSalesDashboard() {
        try {
            log.info("Generating sales dashboard");
            
            // Sales metrics
            long totalSalesRecords = salesRecordRepository.count();
            long closedWonSales = salesRecordRepository.getCountByStatus(SalesRecord.SalesStatus.CLOSED_WON);
            long closedLostSales = salesRecordRepository.getCountByStatus(SalesRecord.SalesStatus.CLOSED_LOST);
            BigDecimal totalRevenue = salesRecordRepository.getTotalClosedWonAmount();
            BigDecimal averageDealSize = salesRecordRepository.getAverageClosedWonAmount();
            
            // Marketing companies metrics
            long totalMarketingCompanies = marketingCompanyRepository.count();
            long activePartners = marketingCompanyRepository.getCountByPartnershipStatus(MarketingCompany.PartnershipStatus.PARTNER);
            long prospectCompanies = marketingCompanyRepository.getCountByPartnershipStatus(MarketingCompany.PartnershipStatus.PROSPECT);
            BigDecimal totalPotentialDealValue = marketingCompanyRepository.getTotalPotentialDealValue();
            
            // Influencer metrics
            long totalInfluencers = influencerRepository.count();
            long activeInfluencers = influencerRepository.getCountByNiche(Influencer.InfluencerNiche.LIFESTYLE); // Example
            long verifiedInfluencers = influencerRepository.getVerifiedInfluencersCount();
            BigDecimal totalInfluencerSpend = influencerRepository.getTotalSpentOnInfluencers();
            BigDecimal averageEngagementRate = influencerRepository.getAverageEngagementRate();
            
            // Build dashboard response
            return SalesDashboardResponse.builder()
                .totalSalesRecords(totalSalesRecords)
                .activeSalesRecords(totalSalesRecords - closedWonSales - closedLostSales)
                .closedWonSales(closedWonSales)
                .closedLostSales(closedLostSales)
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .averageDealSize(averageDealSize != null ? averageDealSize : BigDecimal.ZERO)
                .conversionRate(totalSalesRecords > 0 ? 
                    BigDecimal.valueOf(closedWonSales * 100.0 / totalSalesRecords) : BigDecimal.ZERO)
                .totalMarketingCompanies(totalMarketingCompanies)
                .activePartners(activePartners)
                .prospectCompanies(prospectCompanies)
                .totalPotentialDealValue(totalPotentialDealValue != null ? totalPotentialDealValue : BigDecimal.ZERO)
                .totalInfluencers(totalInfluencers)
                .activeInfluencers(activeInfluencers)
                .verifiedInfluencers(verifiedInfluencers)
                .totalInfluencerSpend(totalInfluencerSpend != null ? totalInfluencerSpend : BigDecimal.ZERO)
                .averageEngagementRate(averageEngagementRate != null ? averageEngagementRate : BigDecimal.ZERO)
                .salesByStatus(getSalesByStatusMap())
                .companiesByIndustry(getCompaniesByIndustryMap())
                .influencersByNiche(getInfluencersByNicheMap())
                .build();
                
        } catch (Exception e) {
            log.error("Error generating sales dashboard: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate sales dashboard", e);
        }
    }

    @Override
    public SalesRecordResponse createSalesRecord(SalesRecordRequest request) {
        try {
            log.info("Creating sales record for client: {}", request.getClientName());
            
            Employee salesEmployee = null;
            if (request.getSalesEmployeeId() != null) {
                salesEmployee = employeeRepository.findById(request.getSalesEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Sales employee not found"));
            }
            
            SalesRecord salesRecord = SalesRecord.builder()
                .clientName(request.getClientName())
                .clientEmail(request.getClientEmail())
                .clientPhone(request.getClientPhone())
                .clientCompany(request.getClientCompany())
                .productService(request.getProductService())
                .amount(request.getAmount())
                .commission(request.getCommission())
                .status(request.getStatus())
                .priority(request.getPriority())
                .source(request.getSource())
                .saleDate(request.getSaleDate() != null ? request.getSaleDate() : LocalDate.now())
                .expectedCloseDate(request.getExpectedCloseDate())
                .actualCloseDate(request.getActualCloseDate())
                .salesEmployee(salesEmployee)
                .notes(request.getNotes())
                .description(request.getDescription())
                .discount(request.getDiscount())
                .tax(request.getTax())
                .finalAmount(request.getFinalAmount())
                .followUpCount(request.getFollowUpCount())
                .lastFollowUpDate(request.getLastFollowUpDate())
                .nextFollowUpDate(request.getNextFollowUpDate())
                .daysToClose(request.getDaysToClose())
                .conversionRate(request.getConversionRate())
                .build();
            
            salesRecord = salesRecordRepository.save(salesRecord);
            log.info("Sales record created successfully with ID: {}", salesRecord.getId());
            
            return mapToSalesRecordResponse(salesRecord);
            
        } catch (Exception e) {
            log.error("Error creating sales record: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create sales record", e);
        }
    }

    @Override
    public SalesRecordResponse updateSalesRecord(String id, SalesRecordRequest request) {
        try {
            log.info("Updating sales record: {}", id);
            
            SalesRecord salesRecord = salesRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales record not found with id: " + id));
            
            Employee salesEmployee = null;
            if (request.getSalesEmployeeId() != null) {
                salesEmployee = employeeRepository.findById(request.getSalesEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Sales employee not found"));
            }
            
            // Update fields
            salesRecord.setClientName(request.getClientName());
            salesRecord.setClientEmail(request.getClientEmail());
            salesRecord.setClientPhone(request.getClientPhone());
            salesRecord.setClientCompany(request.getClientCompany());
            salesRecord.setProductService(request.getProductService());
            salesRecord.setAmount(request.getAmount());
            salesRecord.setCommission(request.getCommission());
            salesRecord.setStatus(request.getStatus());
            salesRecord.setPriority(request.getPriority());
            salesRecord.setSource(request.getSource());
            salesRecord.setSaleDate(request.getSaleDate() != null ? request.getSaleDate() : salesRecord.getSaleDate());
            salesRecord.setExpectedCloseDate(request.getExpectedCloseDate());
            salesRecord.setActualCloseDate(request.getActualCloseDate());
            salesRecord.setSalesEmployee(salesEmployee);
            salesRecord.setNotes(request.getNotes());
            salesRecord.setDescription(request.getDescription());
            salesRecord.setDiscount(request.getDiscount());
            salesRecord.setTax(request.getTax());
            salesRecord.setFinalAmount(request.getFinalAmount());
            salesRecord.setFollowUpCount(request.getFollowUpCount());
            salesRecord.setLastFollowUpDate(request.getLastFollowUpDate());
            salesRecord.setNextFollowUpDate(request.getNextFollowUpDate());
            salesRecord.setDaysToClose(request.getDaysToClose());
            salesRecord.setConversionRate(request.getConversionRate());
            
            salesRecord = salesRecordRepository.save(salesRecord);
            log.info("Sales record updated successfully: {}", id);
            
            return mapToSalesRecordResponse(salesRecord);
            
        } catch (Exception e) {
            log.error("Error updating sales record: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update sales record", e);
        }
    }

    @Override
    public SalesRecordResponse getSalesRecordById(String id) {
        try {
            SalesRecord salesRecord = salesRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales record not found with id: " + id));
            return mapToSalesRecordResponse(salesRecord);
        } catch (Exception e) {
            log.error("Error fetching sales record: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch sales record", e);
        }
    }

    @Override
    public Page<SalesRecordResponse> getAllSalesRecords(int page, int size, String sortBy, String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<SalesRecord> salesRecords = salesRecordRepository.findAll(pageable);
            return salesRecords.map(this::mapToSalesRecordResponse);
        } catch (Exception e) {
            log.error("Error fetching sales records: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch sales records", e);
        }
    }

    @Override
    public void deleteSalesRecord(String id) {
        try {
            SalesRecord salesRecord = salesRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales record not found with id: " + id));
            
            salesRecordRepository.delete(salesRecord);
            log.info("Sales record deleted successfully: {}", id);
        } catch (Exception e) {
            log.error("Error deleting sales record: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete sales record", e);
        }
    }

    // Helper methods
    private SalesRecordResponse mapToSalesRecordResponse(SalesRecord salesRecord) {
        SalesRecordResponse.SalesRecordResponseBuilder builder = SalesRecordResponse.builder()
            .id(salesRecord.getId())
            .clientName(salesRecord.getClientName())
            .clientEmail(salesRecord.getClientEmail())
            .clientPhone(salesRecord.getClientPhone())
            .clientCompany(salesRecord.getClientCompany())
            .productService(salesRecord.getProductService())
            .amount(salesRecord.getAmount())
            .commission(salesRecord.getCommission())
            .status(salesRecord.getStatus())
            .statusDisplayName(salesRecord.getStatus() != null ? salesRecord.getStatus().name() : null)
            .priority(salesRecord.getPriority())
            .priorityDisplayName(salesRecord.getPriority() != null ? salesRecord.getPriority().name() : null)
            .source(salesRecord.getSource())
            .sourceDisplayName(salesRecord.getSource() != null ? salesRecord.getSource().name() : null)
            .saleDate(salesRecord.getSaleDate())
            .expectedCloseDate(salesRecord.getExpectedCloseDate())
            .actualCloseDate(salesRecord.getActualCloseDate())
            .notes(salesRecord.getNotes())
            .description(salesRecord.getDescription())
            .discount(salesRecord.getDiscount())
            .tax(salesRecord.getTax())
            .finalAmount(salesRecord.getFinalAmount())
            .followUpCount(salesRecord.getFollowUpCount())
            .lastFollowUpDate(salesRecord.getLastFollowUpDate())
            .nextFollowUpDate(salesRecord.getNextFollowUpDate())
            .daysToClose(salesRecord.getDaysToClose())
            .conversionRate(salesRecord.getConversionRate())
            .createdAt(salesRecord.getCreatedAt())
            .updatedAt(salesRecord.getUpdatedAt())
            .createdBy(salesRecord.getCreatedBy())
            .updatedBy(salesRecord.getUpdatedBy());

        // Map sales employee information
        if (salesRecord.getSalesEmployee() != null) {
            Employee emp = salesRecord.getSalesEmployee();
            builder
                .salesEmployeeId(emp.getId())
                .salesEmployeeName(emp.getPerson() != null ? 
                    emp.getPerson().getFirstName() + " " + emp.getPerson().getLastName() : "Unknown")
                .salesEmployeeEmail(""); // Email not available in Person entity
        }

        // Computed fields
        builder
            .isOverdue(salesRecord.getExpectedCloseDate() != null && 
                salesRecord.getExpectedCloseDate().isBefore(LocalDate.now()) && 
                salesRecord.getStatus() != SalesRecord.SalesStatus.CLOSED_WON &&
                salesRecord.getStatus() != SalesRecord.SalesStatus.CLOSED_LOST)
            .isHighValue(salesRecord.getAmount() != null && 
                salesRecord.getAmount().compareTo(BigDecimal.valueOf(10000)) > 0)
            .needsFollowUp(salesRecord.getNextFollowUpDate() != null && 
                salesRecord.getNextFollowUpDate().isBefore(LocalDate.now().plusDays(1)))
            .daysUntilExpectedClose(salesRecord.getExpectedCloseDate() != null ? 
                ChronoUnit.DAYS.between(LocalDate.now(), salesRecord.getExpectedCloseDate()) : null);

        return builder.build();
    }

    private Map<String, Long> getSalesByStatusMap() {
        List<Object[]> results = salesRecordRepository.getSalesByStatus();
        return results.stream()
            .collect(Collectors.toMap(
                result -> result[0].toString(),
                result -> (Long) result[1]
            ));
    }

    private Map<String, Long> getCompaniesByIndustryMap() {
        List<Object[]> results = marketingCompanyRepository.getCompaniesByIndustry();
        return results.stream()
            .collect(Collectors.toMap(
                result -> result[0].toString(),
                result -> (Long) result[1]
            ));
    }

    private Map<String, Long> getInfluencersByNicheMap() {
        List<Object[]> results = influencerRepository.getInfluencersByNiche();
        return results.stream()
            .collect(Collectors.toMap(
                result -> result[0].toString(),
                result -> (Long) result[1]
            ));
    }

    // Placeholder implementations for other methods
    @Override
    public Map<String, Object> getSalesStatistics() {
        return Map.of(
            "totalSales", salesRecordRepository.count(),
            "totalRevenue", salesRecordRepository.getTotalClosedWonAmount() != null ? 
                salesRecordRepository.getTotalClosedWonAmount() : BigDecimal.ZERO
        );
    }

    @Override
    public Map<String, Object> getMarketingStatistics() {
        return Map.of(
            "totalCompanies", marketingCompanyRepository.count(),
            "activePartners", marketingCompanyRepository.getCountByPartnershipStatus(MarketingCompany.PartnershipStatus.PARTNER)
        );
    }

    @Override
    public Map<String, Object> getInfluencerStatistics() {
        return Map.of(
            "totalInfluencers", influencerRepository.count(),
            "verifiedInfluencers", influencerRepository.getVerifiedInfluencersCount()
        );
    }

    // Marketing Company CRUD Operations
    @Override
    public MarketingCompanyResponse createMarketingCompany(MarketingCompanyRequest request) {
        try {
            log.info("Creating marketing company: {}", request.getCompanyName());

            MarketingCompany company = MarketingCompany.builder()
                .companyName(request.getCompanyName())
                .industry(request.getIndustry())
                .website(request.getWebsite())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .contactPersonName(request.getContactPersonName())
                .contactPersonTitle(request.getContactPersonTitle())
                .contactPersonEmail(request.getContactPersonEmail())
                .contactPersonPhone(request.getContactPersonPhone())
                .companySize(request.getCompanySize())
                .partnershipStatus(request.getPartnershipStatus())
                .rating(request.getRating())
                .annualRevenue(request.getAnnualRevenue())
                .marketValue(request.getMarketValue())
                .potentialDealValue(request.getPotentialDealValue())
                .partnershipStartDate(request.getPartnershipStartDate())
                .lastContactDate(request.getLastContactDate())
                .nextContactDate(request.getNextContactDate())
                .services(request.getServices())
                .notes(request.getNotes())
                .description(request.getDescription())
                .linkedinUrl(request.getLinkedinUrl())
                .facebookUrl(request.getFacebookUrl())
                .twitterUrl(request.getTwitterUrl())
                .instagramUrl(request.getInstagramUrl())
                .logoUrl(request.getLogoUrl())
                .brandColors(request.getBrandColors())
                .employeeCount(request.getEmployeeCount())
                .yearsInBusiness(request.getYearsInBusiness())
                .satisfactionScore(request.getSatisfactionScore())
                .country(request.getCountry())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .build();

            company = marketingCompanyRepository.save(company);
            log.info("Marketing company created successfully with ID: {}", company.getId());

            return mapToMarketingCompanyResponse(company);

        } catch (Exception e) {
            log.error("Error creating marketing company: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create marketing company", e);
        }
    }

    @Override
    public MarketingCompanyResponse updateMarketingCompany(String id, MarketingCompanyRequest request) {
        try {
            log.info("Updating marketing company: {}", id);

            MarketingCompany company = marketingCompanyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marketing company not found with id: " + id));

            // Update all fields
            company.setCompanyName(request.getCompanyName());
            company.setIndustry(request.getIndustry());
            company.setWebsite(request.getWebsite());
            company.setEmail(request.getEmail());
            company.setPhone(request.getPhone());
            company.setAddress(request.getAddress());
            company.setContactPersonName(request.getContactPersonName());
            company.setContactPersonTitle(request.getContactPersonTitle());
            company.setContactPersonEmail(request.getContactPersonEmail());
            company.setContactPersonPhone(request.getContactPersonPhone());
            company.setCompanySize(request.getCompanySize());
            company.setPartnershipStatus(request.getPartnershipStatus());
            company.setRating(request.getRating());
            company.setAnnualRevenue(request.getAnnualRevenue());
            company.setMarketValue(request.getMarketValue());
            company.setPotentialDealValue(request.getPotentialDealValue());
            company.setPartnershipStartDate(request.getPartnershipStartDate());
            company.setLastContactDate(request.getLastContactDate());
            company.setNextContactDate(request.getNextContactDate());
            company.setServices(request.getServices());
            company.setNotes(request.getNotes());
            company.setDescription(request.getDescription());
            company.setLinkedinUrl(request.getLinkedinUrl());
            company.setFacebookUrl(request.getFacebookUrl());
            company.setTwitterUrl(request.getTwitterUrl());
            company.setInstagramUrl(request.getInstagramUrl());
            company.setLogoUrl(request.getLogoUrl());
            company.setBrandColors(request.getBrandColors());
            company.setEmployeeCount(request.getEmployeeCount());
            company.setYearsInBusiness(request.getYearsInBusiness());
            company.setSatisfactionScore(request.getSatisfactionScore());
            company.setCountry(request.getCountry());
            company.setCity(request.getCity());
            company.setState(request.getState());
            company.setZipCode(request.getZipCode());

            company = marketingCompanyRepository.save(company);
            log.info("Marketing company updated successfully: {}", id);

            return mapToMarketingCompanyResponse(company);

        } catch (Exception e) {
            log.error("Error updating marketing company: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update marketing company", e);
        }
    }

    @Override
    public MarketingCompanyResponse getMarketingCompanyById(String id) {
        try {
            MarketingCompany company = marketingCompanyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marketing company not found with id: " + id));
            return mapToMarketingCompanyResponse(company);
        } catch (Exception e) {
            log.error("Error fetching marketing company: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch marketing company", e);
        }
    }

    @Override
    public Page<MarketingCompanyResponse> getAllMarketingCompanies(int page, int size, String sortBy, String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<MarketingCompany> companies = marketingCompanyRepository.findAll(pageable);
            return companies.map(this::mapToMarketingCompanyResponse);
        } catch (Exception e) {
            log.error("Error fetching marketing companies: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch marketing companies", e);
        }
    }

    @Override
    public void deleteMarketingCompany(String id) {
        try {
            MarketingCompany company = marketingCompanyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marketing company not found with id: " + id));

            marketingCompanyRepository.delete(company);
            log.info("Marketing company deleted successfully: {}", id);
        } catch (Exception e) {
            log.error("Error deleting marketing company: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete marketing company", e);
        }
    }

    private MarketingCompanyResponse mapToMarketingCompanyResponse(MarketingCompany company) {
        return MarketingCompanyResponse.builder()
            .id(company.getId())
            .companyName(company.getCompanyName())
            .industry(company.getIndustry())
            .website(company.getWebsite())
            .email(company.getEmail())
            .phone(company.getPhone())
            .address(company.getAddress())
            .contactPersonName(company.getContactPersonName())
            .contactPersonTitle(company.getContactPersonTitle())
            .contactPersonEmail(company.getContactPersonEmail())
            .contactPersonPhone(company.getContactPersonPhone())
            .companySize(company.getCompanySize())
            .companySizeDisplayName(company.getCompanySize() != null ? company.getCompanySize().name() : null)
            .partnershipStatus(company.getPartnershipStatus())
            .partnershipStatusDisplayName(company.getPartnershipStatus() != null ? company.getPartnershipStatus().name() : null)
            .rating(company.getRating())
            .ratingDisplayName(company.getRating() != null ? company.getRating().name() : null)
            .annualRevenue(company.getAnnualRevenue())
            .marketValue(company.getMarketValue())
            .potentialDealValue(company.getPotentialDealValue())
            .partnershipStartDate(company.getPartnershipStartDate())
            .lastContactDate(company.getLastContactDate())
            .nextContactDate(company.getNextContactDate())
            .services(company.getServices())
            .notes(company.getNotes())
            .description(company.getDescription())
            .linkedinUrl(company.getLinkedinUrl())
            .facebookUrl(company.getFacebookUrl())
            .twitterUrl(company.getTwitterUrl())
            .instagramUrl(company.getInstagramUrl())
            .logoUrl(company.getLogoUrl())
            .brandColors(company.getBrandColors())
            .employeeCount(company.getEmployeeCount())
            .yearsInBusiness(company.getYearsInBusiness())
            .satisfactionScore(company.getSatisfactionScore())
            .country(company.getCountry())
            .city(company.getCity())
            .state(company.getState())
            .zipCode(company.getZipCode())
            .needsFollowUp(company.getNextContactDate() != null &&
                company.getNextContactDate().isBefore(LocalDate.now().plusDays(1)))
            .daysSinceLastContact(company.getLastContactDate() != null ?
                ChronoUnit.DAYS.between(company.getLastContactDate(), LocalDate.now()) : null)
            .isHighValue(company.getPotentialDealValue() != null &&
                company.getPotentialDealValue().compareTo(BigDecimal.valueOf(50000)) > 0)
            .createdAt(company.getCreatedAt())
            .updatedAt(company.getUpdatedAt())
            .createdBy(company.getCreatedBy())
            .updatedBy(company.getUpdatedBy())
            .build();
    }

    // Placeholder implementations for remaining methods
    @Override
    public Page<SalesRecordResponse> searchSalesRecords(String searchTerm, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<SalesRecord> salesRecords = salesRecordRepository.searchSalesRecords(searchTerm, pageable);
            return salesRecords.map(this::mapToSalesRecordResponse);
        } catch (Exception e) {
            log.error("Error searching sales records: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to search sales records", e);
        }
    }

    @Override
    public Page<SalesRecordResponse> getSalesRecordsByStatus(String status, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            SalesRecord.SalesStatus salesStatus = SalesRecord.SalesStatus.valueOf(status);
            Page<SalesRecord> salesRecords = salesRecordRepository.findByStatus(salesStatus, pageable);
            return salesRecords.map(this::mapToSalesRecordResponse);
        } catch (Exception e) {
            log.error("Error fetching sales records by status: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch sales records by status", e);
        }
    }

    @Override
    public Page<SalesRecordResponse> getSalesRecordsByEmployee(String employeeId, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<SalesRecord> salesRecords = salesRecordRepository.findBySalesEmployeeId(employeeId, pageable);
            return salesRecords.map(this::mapToSalesRecordResponse);
        } catch (Exception e) {
            log.error("Error fetching sales records by employee: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch sales records by employee", e);
        }
    }

    @Override
    public Page<SalesRecordResponse> getSalesRecordsByDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<SalesRecord> salesRecords = salesRecordRepository.findBySaleDateBetween(startDate, endDate, pageable);
            return salesRecords.map(this::mapToSalesRecordResponse);
        } catch (Exception e) {
            log.error("Error fetching sales records by date range: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch sales records by date range", e);
        }
    }

    @Override
    public List<SalesRecordResponse> getOverdueFollowUps() {
        try {
            List<SalesRecord> overdueRecords = salesRecordRepository.getOverdueFollowUps(LocalDate.now());
            return overdueRecords.stream()
                .map(this::mapToSalesRecordResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching overdue follow-ups: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch overdue follow-ups", e);
        }
    }

    @Override
    public Page<MarketingCompanyResponse> searchMarketingCompanies(String searchTerm, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<MarketingCompany> companies = marketingCompanyRepository.searchMarketingCompanies(searchTerm, pageable);
            return companies.map(this::mapToMarketingCompanyResponse);
        } catch (Exception e) {
            log.error("Error searching marketing companies: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to search marketing companies", e);
        }
    }

    @Override
    public Page<MarketingCompanyResponse> getMarketingCompaniesByIndustry(String industry, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<MarketingCompany> companies = marketingCompanyRepository.findByIndustryContainingIgnoreCase(industry, pageable);
            return companies.map(this::mapToMarketingCompanyResponse);
        } catch (Exception e) {
            log.error("Error fetching marketing companies by industry: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch marketing companies by industry", e);
        }
    }

    @Override
    public Page<MarketingCompanyResponse> getMarketingCompaniesByStatus(String status, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            MarketingCompany.PartnershipStatus partnershipStatus = MarketingCompany.PartnershipStatus.valueOf(status);
            Page<MarketingCompany> companies = marketingCompanyRepository.findByPartnershipStatus(partnershipStatus, pageable);
            return companies.map(this::mapToMarketingCompanyResponse);
        } catch (Exception e) {
            log.error("Error fetching marketing companies by status: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch marketing companies by status", e);
        }
    }

    @Override
    public List<MarketingCompanyResponse> getCompaniesNeedingFollowUp() {
        try {
            List<MarketingCompany> companies = marketingCompanyRepository.getCompaniesNeedingFollowUp();
            return companies.stream()
                .map(this::mapToMarketingCompanyResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching companies needing follow-up: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch companies needing follow-up", e);
        }
    }

    // Influencer CRUD Operations
    @Override
    public InfluencerResponse createInfluencer(InfluencerRequest request) {
        try {
            log.info("Creating influencer: {}", request.getFullName());

            Influencer influencer = Influencer.builder()
                .fullName(request.getFullName())
                .instagramHandle(request.getInstagramHandle())
                .email(request.getEmail())
                .phone(request.getPhone())
                .niche(request.getNiche())
                .tier(request.getTier())
                .collaborationStatus(request.getCollaborationStatus())
                .followersCount(request.getFollowersCount())
                .followingCount(request.getFollowingCount())
                .postsCount(request.getPostsCount())
                .engagementRate(request.getEngagementRate())
                .averageLikes(request.getAverageLikes())
                .averageComments(request.getAverageComments())
                .country(request.getCountry())
                .city(request.getCity())
                .age(request.getAge())
                .gender(request.getGender())
                .language(request.getLanguage())
                .bio(request.getBio())
                .profilePictureUrl(request.getProfilePictureUrl())
                .isVerified(request.getIsVerified())
                .isBusinessAccount(request.getIsBusinessAccount())
                .ratePerPost(request.getRatePerPost())
                .ratePerStory(request.getRatePerStory())
                .ratePerReel(request.getRatePerReel())
                .currency(request.getCurrency())
                .reputation(request.getReputation())
                .responseRate(request.getResponseRate())
                .deliveryRate(request.getDeliveryRate())
                .qualityScore(request.getQualityScore())
                .lastContactDate(request.getLastContactDate())
                .lastCollaborationDate(request.getLastCollaborationDate())
                .totalCollaborations(request.getTotalCollaborations())
                .totalSpent(request.getTotalSpent())
                .notes(request.getNotes())
                .specialties(request.getSpecialties())
                .previousBrands(request.getPreviousBrands())
                .youtubeUrl(request.getYoutubeUrl())
                .tiktokUrl(request.getTiktokUrl())
                .twitterUrl(request.getTwitterUrl())
                .linkedinUrl(request.getLinkedinUrl())
                .websiteUrl(request.getWebsiteUrl())
                .contentStyle(request.getContentStyle())
                .preferredBrands(request.getPreferredBrands())
                .contentLanguages(request.getContentLanguages())
                .build();

            influencer = influencerRepository.save(influencer);
            log.info("Influencer created successfully with ID: {}", influencer.getId());

            return mapToInfluencerResponse(influencer);

        } catch (Exception e) {
            log.error("Error creating influencer: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create influencer", e);
        }
    }

    @Override
    public InfluencerResponse updateInfluencer(String id, InfluencerRequest request) {
        try {
            log.info("Updating influencer: {}", id);

            Influencer influencer = influencerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Influencer not found with id: " + id));

            // Update all fields
            influencer.setFullName(request.getFullName());
            influencer.setInstagramHandle(request.getInstagramHandle());
            influencer.setEmail(request.getEmail());
            influencer.setPhone(request.getPhone());
            influencer.setNiche(request.getNiche());
            influencer.setTier(request.getTier());
            influencer.setCollaborationStatus(request.getCollaborationStatus());
            influencer.setFollowersCount(request.getFollowersCount());
            influencer.setFollowingCount(request.getFollowingCount());
            influencer.setPostsCount(request.getPostsCount());
            influencer.setEngagementRate(request.getEngagementRate());
            influencer.setAverageLikes(request.getAverageLikes());
            influencer.setAverageComments(request.getAverageComments());
            influencer.setCountry(request.getCountry());
            influencer.setCity(request.getCity());
            influencer.setAge(request.getAge());
            influencer.setGender(request.getGender());
            influencer.setLanguage(request.getLanguage());
            influencer.setBio(request.getBio());
            influencer.setProfilePictureUrl(request.getProfilePictureUrl());
            influencer.setIsVerified(request.getIsVerified());
            influencer.setIsBusinessAccount(request.getIsBusinessAccount());
            influencer.setRatePerPost(request.getRatePerPost());
            influencer.setRatePerStory(request.getRatePerStory());
            influencer.setRatePerReel(request.getRatePerReel());
            influencer.setCurrency(request.getCurrency());
            influencer.setReputation(request.getReputation());
            influencer.setResponseRate(request.getResponseRate());
            influencer.setDeliveryRate(request.getDeliveryRate());
            influencer.setQualityScore(request.getQualityScore());
            influencer.setLastContactDate(request.getLastContactDate());
            influencer.setLastCollaborationDate(request.getLastCollaborationDate());
            influencer.setTotalCollaborations(request.getTotalCollaborations());
            influencer.setTotalSpent(request.getTotalSpent());
            influencer.setNotes(request.getNotes());
            influencer.setSpecialties(request.getSpecialties());
            influencer.setPreviousBrands(request.getPreviousBrands());
            influencer.setYoutubeUrl(request.getYoutubeUrl());
            influencer.setTiktokUrl(request.getTiktokUrl());
            influencer.setTwitterUrl(request.getTwitterUrl());
            influencer.setLinkedinUrl(request.getLinkedinUrl());
            influencer.setWebsiteUrl(request.getWebsiteUrl());
            influencer.setContentStyle(request.getContentStyle());
            influencer.setPreferredBrands(request.getPreferredBrands());
            influencer.setContentLanguages(request.getContentLanguages());

            influencer = influencerRepository.save(influencer);
            log.info("Influencer updated successfully: {}", id);

            return mapToInfluencerResponse(influencer);

        } catch (Exception e) {
            log.error("Error updating influencer: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update influencer", e);
        }
    }

    @Override
    public InfluencerResponse getInfluencerById(String id) {
        try {
            Influencer influencer = influencerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Influencer not found with id: " + id));
            return mapToInfluencerResponse(influencer);
        } catch (Exception e) {
            log.error("Error fetching influencer: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch influencer", e);
        }
    }

    @Override
    public Page<InfluencerResponse> getAllInfluencers(int page, int size, String sortBy, String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<Influencer> influencers = influencerRepository.findAll(pageable);
            return influencers.map(this::mapToInfluencerResponse);
        } catch (Exception e) {
            log.error("Error fetching influencers: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch influencers", e);
        }
    }

    @Override
    public void deleteInfluencer(String id) {
        try {
            Influencer influencer = influencerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Influencer not found with id: " + id));

            influencerRepository.delete(influencer);
            log.info("Influencer deleted successfully: {}", id);
        } catch (Exception e) {
            log.error("Error deleting influencer: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete influencer", e);
        }
    }

    @Override
    public Page<InfluencerResponse> searchInfluencers(String searchTerm, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Influencer> influencers = influencerRepository.searchInfluencers(searchTerm, pageable);
            return influencers.map(this::mapToInfluencerResponse);
        } catch (Exception e) {
            log.error("Error searching influencers: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to search influencers", e);
        }
    }

    @Override
    public Page<InfluencerResponse> getInfluencersByNiche(String niche, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Influencer.InfluencerNiche influencerNiche = Influencer.InfluencerNiche.valueOf(niche);
            Page<Influencer> influencers = influencerRepository.findByNiche(influencerNiche, pageable);
            return influencers.map(this::mapToInfluencerResponse);
        } catch (Exception e) {
            log.error("Error fetching influencers by niche: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch influencers by niche", e);
        }
    }

    @Override
    public Page<InfluencerResponse> getInfluencersByTier(String tier, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Influencer.InfluencerTier influencerTier = Influencer.InfluencerTier.valueOf(tier);
            Page<Influencer> influencers = influencerRepository.findByTier(influencerTier, pageable);
            return influencers.map(this::mapToInfluencerResponse);
        } catch (Exception e) {
            log.error("Error fetching influencers by tier: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch influencers by tier", e);
        }
    }

    @Override
    public Page<InfluencerResponse> getTopInfluencersByEngagement(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Influencer> influencers = influencerRepository.findTopPerformersByEngagement(pageable);
            return influencers.map(this::mapToInfluencerResponse);
        } catch (Exception e) {
            log.error("Error fetching top influencers by engagement: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch top influencers by engagement", e);
        }
    }

    @Override
    public Page<InfluencerResponse> getTopInfluencersByFollowers(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Influencer> influencers = influencerRepository.findTopInfluencersByFollowers(pageable);
            return influencers.map(this::mapToInfluencerResponse);
        } catch (Exception e) {
            log.error("Error fetching top influencers by followers: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch top influencers by followers", e);
        }
    }

    @Override
    public List<InfluencerResponse> getInfluencersNeedingFollowUp() {
        try {
            LocalDate cutoffDate = LocalDate.now().minusDays(30);
            List<Influencer> influencers = influencerRepository.getInfluencersNeedingFollowUp(cutoffDate);
            return influencers.stream()
                .map(this::mapToInfluencerResponse)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching influencers needing follow-up: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch influencers needing follow-up", e);
        }
    }

    // Helper method for mapping Influencer to InfluencerResponse (placeholder)
    private InfluencerResponse mapToInfluencerResponse(Influencer influencer) {
        return InfluencerResponse.builder()
            .id(influencer.getId())
            .fullName(influencer.getFullName())
            .instagramHandle(influencer.getInstagramHandle())
            .email(influencer.getEmail())
            .phone(influencer.getPhone())
            .niche(influencer.getNiche())
            .nicheDisplayName(influencer.getNiche() != null ? influencer.getNiche().name() : null)
            .tier(influencer.getTier())
            .tierDisplayName(influencer.getTier() != null ? influencer.getTier().name() : null)
            .collaborationStatus(influencer.getCollaborationStatus())
            .collaborationStatusDisplayName(influencer.getCollaborationStatus() != null ? influencer.getCollaborationStatus().name() : null)
            .followersCount(influencer.getFollowersCount())
            .followersCountFormatted(formatFollowersCount(influencer.getFollowersCount()))
            .followingCount(influencer.getFollowingCount())
            .postsCount(influencer.getPostsCount())
            .engagementRate(influencer.getEngagementRate())
            .averageLikes(influencer.getAverageLikes())
            .averageComments(influencer.getAverageComments())
            .country(influencer.getCountry())
            .city(influencer.getCity())
            .age(influencer.getAge())
            .gender(influencer.getGender())
            .language(influencer.getLanguage())
            .bio(influencer.getBio())
            .profilePictureUrl(influencer.getProfilePictureUrl())
            .isVerified(influencer.getIsVerified())
            .isBusinessAccount(influencer.getIsBusinessAccount())
            .ratePerPost(influencer.getRatePerPost())
            .ratePerStory(influencer.getRatePerStory())
            .ratePerReel(influencer.getRatePerReel())
            .currency(influencer.getCurrency())
            .reputation(influencer.getReputation())
            .reputationDisplayName(influencer.getReputation() != null ? influencer.getReputation().name() : null)
            .responseRate(influencer.getResponseRate())
            .deliveryRate(influencer.getDeliveryRate())
            .qualityScore(influencer.getQualityScore())
            .lastContactDate(influencer.getLastContactDate())
            .lastCollaborationDate(influencer.getLastCollaborationDate())
            .totalCollaborations(influencer.getTotalCollaborations())
            .totalSpent(influencer.getTotalSpent())
            .notes(influencer.getNotes())
            .specialties(influencer.getSpecialties())
            .previousBrands(influencer.getPreviousBrands())
            .youtubeUrl(influencer.getYoutubeUrl())
            .tiktokUrl(influencer.getTiktokUrl())
            .twitterUrl(influencer.getTwitterUrl())
            .linkedinUrl(influencer.getLinkedinUrl())
            .websiteUrl(influencer.getWebsiteUrl())
            .contentStyle(influencer.getContentStyle())
            .preferredBrands(influencer.getPreferredBrands())
            .contentLanguages(influencer.getContentLanguages())
            .isTopPerformer(influencer.getEngagementRate() != null && influencer.getEngagementRate().compareTo(BigDecimal.valueOf(5.0)) > 0)
            .needsFollowUp(influencer.getLastContactDate() != null &&
                influencer.getLastContactDate().isBefore(LocalDate.now().minusDays(30)))
            .daysSinceLastContact(influencer.getLastContactDate() != null ?
                ChronoUnit.DAYS.between(influencer.getLastContactDate(), LocalDate.now()) : null)
            .tierBadge(getTierBadge(influencer.getTier()))
            .performanceScore(calculatePerformanceScore(influencer))
            .createdAt(influencer.getCreatedAt())
            .updatedAt(influencer.getUpdatedAt())
            .createdBy(influencer.getCreatedBy())
            .updatedBy(influencer.getUpdatedBy())
            .build();
    }

    private String formatFollowersCount(Long followersCount) {
        if (followersCount == null) return "0";
        if (followersCount >= 1000000) {
            return String.format("%.1fM", followersCount / 1000000.0);
        } else if (followersCount >= 1000) {
            return String.format("%.1fK", followersCount / 1000.0);
        } else {
            return followersCount.toString();
        }
    }

    private String getTierBadge(Influencer.InfluencerTier tier) {
        if (tier == null) return "UNRATED";
        switch (tier) {
            case MEGA: return "🌟 MEGA";
            case MACRO: return "⭐ MACRO";
            case MID_TIER: return "🔸 MID";
            case MICRO: return "🔹 MICRO";
            case NANO: return "💎 NANO";
            default: return tier.name();
        }
    }

    private String calculatePerformanceScore(Influencer influencer) {
        if (influencer.getEngagementRate() == null) return "N/A";
        double engagement = influencer.getEngagementRate().doubleValue();
        if (engagement >= 6.0) return "EXCELLENT";
        if (engagement >= 4.0) return "GOOD";
        if (engagement >= 2.0) return "AVERAGE";
        return "POOR";
    }

    @Override
    public Map<String, Object> getSalesAnalytics(LocalDate startDate, LocalDate endDate) {
        throw new UnsupportedOperationException("Analytics not implemented yet");
    }

    @Override
    public Map<String, Object> getMarketingAnalytics() {
        throw new UnsupportedOperationException("Analytics not implemented yet");
    }

    @Override
    public Map<String, Object> getInfluencerAnalytics() {
        throw new UnsupportedOperationException("Analytics not implemented yet");
    }

    @Override
    public List<SalesRecordResponse> bulkCreateSalesRecords(List<SalesRecordRequest> requests) {
        throw new UnsupportedOperationException("Bulk operations not implemented yet");
    }

    @Override
    public List<MarketingCompanyResponse> bulkCreateMarketingCompanies(List<MarketingCompanyRequest> requests) {
        throw new UnsupportedOperationException("Bulk operations not implemented yet");
    }

    @Override
    public List<InfluencerResponse> bulkCreateInfluencers(List<InfluencerRequest> requests) {
        throw new UnsupportedOperationException("Bulk operations not implemented yet");
    }
}
