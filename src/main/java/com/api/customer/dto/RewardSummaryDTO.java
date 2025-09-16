package com.api.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardSummaryDTO {
    private Long customerId;
    private String period;          // e.g., "2024-07" or null for all-time
    @JsonProperty("totalReward")
    private int totalPoints;
    private List<TransactionDTO> transactions;
    private List<MonthlyBreakdown> monthlyBreakdown;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyBreakdown {
        private String month;
        private Integer points;
    }
}
