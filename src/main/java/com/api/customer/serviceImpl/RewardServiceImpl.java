package com.api.customer.serviceImpl;

import com.api.customer.dto.RewardSummaryDTO;
import com.api.customer.dto.TransactionDTO;
import com.api.customer.exception.NotFoundException;
import com.api.customer.model.Transaction;
import com.api.customer.repository.TransactionRepository;
import com.api.customer.service.RewardService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RewardServiceImpl implements RewardService {

    private final TransactionRepository repository;

    public RewardServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public RewardSummaryDTO getAllTimeSummary(Long customerId) {
        try {
            List<Transaction> transactions = repository.findByCustomerId(String.valueOf(customerId));
            if (transactions == null || transactions.isEmpty()) {
                throw new NotFoundException("Customer not found or no transactions");
            }

            int total = transactions.stream()
                    .mapToInt(t -> calculateRewardPoints(t.getAmount()))
                    .sum();

            List<TransactionDTO> transactionDtos = transactions.stream()
                    .map(this::mapToTransactionDTO)
                    .collect(Collectors.toList());

            // monthly breakdown
            Map<YearMonth, Integer> monthlyTotals = new TreeMap<>();
            for (Transaction t : transactions) {
                YearMonth ym = YearMonth.from(t.getTransactionDate().atZone(ZoneOffset.UTC));
                monthlyTotals.merge(ym, calculateRewardPoints(t.getAmount()), Integer::sum);
            }

            List<RewardSummaryDTO.MonthlyBreakdown> monthlyBreakdown = monthlyTotals.entrySet().stream()
                    .map(e -> RewardSummaryDTO.MonthlyBreakdown.builder()
                            .month(e.getKey().toString())
                            .points(e.getValue())
                            .build())
                    .collect(Collectors.toList());

            return RewardSummaryDTO.builder()
                    .customerId(customerId)
                    .period(null)
                    .totalPoints(total)
                    .transactions(transactionDtos)
                    .monthlyBreakdown(monthlyBreakdown)
                    .build();

        } catch (DataAccessException ex) {
            throw new RuntimeException("DB error", ex);
        }
    }

    @Override
    public RewardSummaryDTO getMonthlySummary(Long customerId, String yyyyMm) {
        YearMonth yearMonth = YearMonth.parse(yyyyMm);
        Instant start = yearMonth.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant();

        try {
            List<Transaction> transactions = repository.findByCustomerIdAndTransactionDateBetween(
                    String.valueOf(customerId), start, end);
            if (transactions == null || transactions.isEmpty()) {
                throw new NotFoundException("Customer not found or no transactions in month");
            }

            int total = transactions.stream()
                    .mapToInt(t -> calculateRewardPoints(t.getAmount()))
                    .sum();

            List<TransactionDTO> transactionDtos = transactions.stream()
                    .map(this::mapToTransactionDTO)
                    .collect(Collectors.toList());

            return RewardSummaryDTO.builder()
                    .customerId(customerId)
                    .period(yyyyMm)
                    .totalPoints(total)
                    .transactions(transactionDtos)
                    .build();

        } catch (DataAccessException ex) {
            throw new RuntimeException("DB error", ex);
        }
    }

    @Override
    public List<RewardSummaryDTO> getAllCustomersMonthlySummary(String yyyyMm) {
        YearMonth yearMonth = YearMonth.parse(yyyyMm);
        Instant start = yearMonth.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant();

        try {
            List<Transaction> transactions = repository.findByTransactionDateBetween(start, end);
            Map<String, List<Transaction>> byCustomerId =
                    transactions.stream().collect(Collectors.groupingBy(Transaction::getCustomerId));

            List<RewardSummaryDTO> summary = new ArrayList<>();
            for (Map.Entry<String, List<Transaction>> entry : byCustomerId.entrySet()) {
                int total = entry.getValue().stream()
                        .mapToInt(t -> calculateRewardPoints(t.getAmount()))
                        .sum();

                List<TransactionDTO> transactionDtos = entry.getValue().stream()
                        .map(this::mapToTransactionDTO)
                        .collect(Collectors.toList());

                summary.add(RewardSummaryDTO.builder()
                        .customerId(Long.valueOf(entry.getKey()))
                        .period(yyyyMm)
                        .totalPoints(total)
                        .transactions(transactionDtos)
                        .build());
            }
            return summary;
        } catch (DataAccessException ex) {
            throw new RuntimeException("DB error", ex);
        }
    }

    private TransactionDTO mapToTransactionDTO(Transaction transaction) {
        int points = calculateRewardPoints(transaction.getAmount());
        return TransactionDTO.builder()
                .id(transaction.getId())
                .customerId(transaction.getCustomerId())
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .points(points)
                .build();
    }

    // Reward rule: 1 point per dollar over 50, plus 2 per dollar over 100
    private int calculateRewardPoints(Double amount) {
        double value = amount == null ? 0 : amount;
        int points = 0;
        if (value > 100) {
            points += (int) Math.floor(value - 100) * 2;
            value = 100;
        }
        if (value > 50) {
            points += (int) Math.floor(value - 50);
        }
        return points;
    }
}
