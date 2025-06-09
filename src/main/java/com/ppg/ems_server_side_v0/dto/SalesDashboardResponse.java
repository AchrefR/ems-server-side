package com.ppg.ems_server_side_v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesDashboardResponse {
    
    // Sales Overview
    private Long totalSalesRecords;
    private Long activeSalesRecords;
    private Long closedWonSales;
    private Long closedLostSales;
    private BigDecimal totalRevenue;
    private BigDecimal averageDealSize;
    private BigDecimal conversionRate;
    
    // Monthly metrics
    private Long salesThisMonth;
    private BigDecimal revenueThisMonth;
    private Long newLeadsThisMonth;
    private BigDecimal monthlyGrowthRate;
    
    // Sales by status
    private Map<String, Long> salesByStatus;
    private Map<String, BigDecimal> revenueByStatus;
    
    // Sales by source
    private Map<String, Long> salesBySource;
    private Map<String, BigDecimal> revenueBySource;
    
    // Marketing Companies
    private Long totalMarketingCompanies;
    private Long activePartners;
    private Long prospectCompanies;
    private BigDecimal totalPotentialDealValue;
    private Map<String, Long> companiesByIndustry;
    private Map<String, Long> companiesByRating;
    
    // Influencers
    private Long totalInfluencers;
    private Long activeInfluencers;
    private Long verifiedInfluencers;
    private BigDecimal totalInfluencerSpend;
    private BigDecimal averageEngagementRate;
    private Map<String, Long> influencersByNiche;
    private Map<String, Long> influencersByTier;
    
    // Performance metrics
    private List<SalesRecordResponse> topDeals;
    private List<InfluencerResponse> topInfluencers;
    private List<MarketingCompanyResponse> topCompanies;
    
    // Follow-up alerts
    private Long overdueFollowUps;
    private Long upcomingFollowUps;
    private List<SalesRecordResponse> urgentFollowUps;
    
    // Recent activities
    private List<RecentActivityResponse> recentActivities;
    
    // Charts data
    private List<ChartDataPoint> salesTrendData;
    private List<ChartDataPoint> revenueTrendData;
    private List<ChartDataPoint> conversionTrendData;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartDataPoint {
        private String label;
        private BigDecimal value;
        private String period;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentActivityResponse {
        private String id;
        private String type;
        private String description;
        private String entityType;
        private String entityId;
        private String entityName;
        private String timestamp;
        private String userFullName;
    }
}
