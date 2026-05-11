package com.hoaug.movieapi.modules.dashboard.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private List<AdminMetric> metrics;
    private List<AdminWorkloadItem> workload;
    private List<AdminActivity> activities;
    private List<List<Integer>> trendSets;
    private List<Integer> mainTrend;
    private List<AdminMetricGroup> metricGroups;
    private List<AdminDistributionItem> distributions;
    private List<AdminSystemSignal> systemSignals;
    private List<AdminRankingCard> rankingCards;
    private List<AdminServerPerformance> serverPerformance;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminServerPerformance {
        private String label;
        private String color;
        private List<Integer> data;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminMetric {
        private String label;
        private String value;
        private String delta;
        private String tone;
        private String helper;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminMetricGroup {
        private String title;
        private String subtitle;
        private List<AdminMetric> items;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminWorkloadItem {
        private String name;
        private Integer value;
        private String color;
        private String caption;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminDistributionItem {
        private String label;
        private Integer value;
        private String color;
        private String scope;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminSystemSignal {
        private String label;
        private String value;
        private String status;
        private String detail;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminRankingCard {
        private String title;
        private String subtitle;
        private String accent;
        private List<AdminRankingItem> items;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminRankingItem {
        private Long id;
        private String slug;
        private String href;
        private String title;
        private String value;
        private String detail;
        private String meta;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminActivity {
        private String id;
        private String title;
        private String description;
        private String severity;
        private String time;
    }
}
