package com.api.customer.controller;

import com.api.customer.dto.RewardSummaryDTO;
import com.api.customer.service.RewardService;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/customers/{customerId}/rewards")
    public ResponseEntity<RewardSummaryDTO> getAllTimeRewards(@PathVariable Long customerId) {
        return ResponseEntity.ok(rewardService.getAllTimeSummary(customerId));
    }

    @GetMapping(value = "/customers/{customerId}/rewards", params = "month")
    public ResponseEntity<RewardSummaryDTO> getMonthlyRewards(
            @PathVariable Long customerId,
            @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}$") String month) {
        return ResponseEntity.ok(rewardService.getMonthlySummary(customerId, month));
    }

    @GetMapping(value = "/rewards", params = "month")
    public ResponseEntity<List<RewardSummaryDTO>> getAllCustomersMonthlyRewards(
            @RequestParam @Pattern(regexp = "^\\d{4}-\\d{2}$") String month) {
        return ResponseEntity.ok(rewardService.getAllCustomersMonthlySummary(month));
    }
}
