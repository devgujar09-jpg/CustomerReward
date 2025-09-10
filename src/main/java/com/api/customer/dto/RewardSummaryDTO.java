package com.api.customer.dto;

import java.util.List;


public class RewardSummaryDTO {
    private Long customerId;
    private String month;
    private Integer totalReward;
    private List<TransactionDTO> transactions;
    private List<MonthlyBreakdown> monthlyBreakdown;

    public RewardSummaryDTO() {}

    public RewardSummaryDTO(Long customerId, String month, Integer totalReward, List<TransactionDTO> transactions) {
        this.customerId = customerId;
        this.month = month;
        this.totalReward = totalReward;
        this.transactions = transactions;
    }

    public static class MonthlyBreakdown {
        private String month;
        private Integer reward;
        public MonthlyBreakdown() {}
        public MonthlyBreakdown(String month, Integer reward) { this.month = month; this.reward = reward; }
        public String getMonth() { return month; }
        public Integer getReward() { return reward; }
        public void setMonth(String month) { this.month = month; }
        public void setReward(Integer reward) { this.reward = reward; }
    }

    public Long getCustomerId() { return customerId; }
    public String getMonth() { return month; }
    public Integer getTotalReward() { return totalReward; }
    public List<TransactionDTO> getTransactions() { return transactions; }
    public List<MonthlyBreakdown> getMonthlyBreakdown() { return monthlyBreakdown; }

    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public void setMonth(String month) { this.month = month; }
    public void setTotalReward(Integer totalReward) { this.totalReward = totalReward; }
    public void setTransactions(List<TransactionDTO> transactions) { this.transactions = transactions; }
    public void setMonthlyBreakdown(List<MonthlyBreakdown> monthlyBreakdown) { this.monthlyBreakdown = monthlyBreakdown; }
}
