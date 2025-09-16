package com.api.customer.service;

import com.api.customer.dto.RewardSummaryDTO;

import java.util.List;

public interface RewardService {
    RewardSummaryDTO getAllTimeSummary(Long customerId);
    RewardSummaryDTO getMonthlySummary(Long customerId, String yyyyMm);
    List<RewardSummaryDTO> getAllCustomersMonthlySummary(String yyyyMm);
}