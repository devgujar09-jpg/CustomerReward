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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Collections;

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
            List<Transaction> txs = repository.findByCustomerId(customerId);
            if (txs == null || txs.isEmpty()) throw new NotFoundException("Customer not found or no transactions");
            int total = txs.stream().mapToInt(t -> calculateRewardPoints(t.getAmount())).sum();
            List<TransactionDTO> dtoTx = txs.stream().map(this::mapToTransactionDTO).collect(Collectors.toList());
            RewardSummaryDTO res = new RewardSummaryDTO(customerId, null, total, dtoTx);
            // build monthly breakdown
            Map<YearMonth, Integer> map = new TreeMap<>();
            for (Transaction t: txs) {
                YearMonth ym = YearMonth.from(t.getDate());
                map.put(ym, map.getOrDefault(ym,0) + calculateRewardPoints(t.getAmount()));
            }
            List<RewardSummaryDTO.MonthlyBreakdown> breakdown = map.entrySet().stream()
                    .map(e -> new RewardSummaryDTO.MonthlyBreakdown(e.getKey().toString(), e.getValue()))
                    .collect(Collectors.toList());
            res.setMonthlyBreakdown(breakdown);
            return res;
        } catch (DataAccessException e) {
            throw new RuntimeException("DB error", e);
        }
    }

    @Override
    public RewardSummaryDTO getMonthlySummary(Long customerId, String yyyyMm) {
        YearMonth ym = YearMonth.parse(yyyyMm);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(LocalTime.MAX);
        try {
            List<Transaction> txs = repository.findByCustomerIdAndDateBetween(customerId, start, end);
            if (txs == null || txs.isEmpty()) throw new NotFoundException("Customer not found or no transactions in month");
            int total = txs.stream().mapToInt(t -> calculateRewardPoints(t.getAmount())).sum();
            List<TransactionDTO> dtoTx = txs.stream().map(this::mapToTransactionDTO).collect(Collectors.toList());
            return new RewardSummaryDTO(customerId, yyyyMm, total, dtoTx);
        } catch (DataAccessException e) {
            throw new RuntimeException("DB error", e);
        }
    }

    @Override
    public List<RewardSummaryDTO> getAllCustomersMonthlySummary(String yyyyMm) {
        YearMonth ym = YearMonth.parse(yyyyMm);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(LocalTime.MAX);
        try {
            List<Transaction> txs = repository.findByDateBetween(start, end);
            Map<Long, List<Transaction>> byCustomer = txs.stream().collect(Collectors.groupingBy(Transaction::getCustomerId));
            List<RewardSummaryDTO> res = new java.util.ArrayList<>();
            for (Map.Entry<Long, List<Transaction>> entry : byCustomer.entrySet()) {
                int total = entry.getValue().stream().mapToInt(t -> calculateRewardPoints(t.getAmount())).sum();
                List<TransactionDTO> dtoTx = entry.getValue().stream().map(this::mapToTransactionDTO).collect(Collectors.toList());
                res.add(new RewardSummaryDTO(entry.getKey(), yyyyMm, total, dtoTx));
            }
            return res;
        } catch (DataAccessException e) {
            throw new RuntimeException("DB error", e);
        }
    }

    private TransactionDTO mapToTransactionDTO(Transaction t) {
        int points = calculateRewardPoints(t.getAmount());
        return new TransactionDTO(t.getId(), t.getCustomerId(), t.getAmount(), t.getDate(), points);
    }

    private int calculateRewardPoints(Double amt) {
        double amount = amt == null ? 0 : amt;
        int points = 0;
        if (amount > 100) {
            points += (int)Math.floor(amount - 100) * 2;
            amount = 100;
        }
        if (amount > 50) {
            points += (int)Math.floor(amount - 50);
        }
        return points;
    }
}
