package com.api.customer.serviceImpl;

import com.api.customer.dto.TransactionDTO;
import com.api.customer.exception.InvalidDateRangeException;
import com.api.customer.exception.NotFoundException;
import com.api.customer.model.Transaction;
import com.api.customer.repository.TransactionRepository;
import com.api.customer.service.TransactionService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByCustomerId(Long customerId) {
        try {
            List<Transaction> transactions = repository.findByCustomerId(String.valueOf(customerId));
            if (transactions == null || transactions.isEmpty()) {
                throw new NotFoundException("Customer not found or no transactions");
            }
            return transactions.stream().map(this::mapToTransactionDTO).collect(Collectors.toList());
        } catch (DataAccessException ex) {
            throw new RuntimeException("DB error", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByCustomerIdAndDateRange(Long customerId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("startDate must be before or equal to endDate");
        }
        Instant start = startDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = endDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant();

        try {
            List<Transaction> transactions = repository.findByCustomerIdAndTransactionDateBetween(
                    String.valueOf(customerId), start, end);
            if (transactions == null || transactions.isEmpty()) {
                throw new NotFoundException("Customer not found or no transactions in range");
            }
            return transactions.stream().map(this::mapToTransactionDTO).collect(Collectors.toList());
        } catch (DataAccessException ex) {
            throw new RuntimeException("DB error", ex);
        }
    }

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDto) {
        try {
            Transaction newTransaction = Transaction.builder()
                    .customerId(transactionDto.getCustomerId())
                    .amount(transactionDto.getAmount())
                    .transactionDate(transactionDto.getTransactionDate())
                    .build();
            Transaction saved = repository.save(newTransaction);
            return mapToTransactionDTO(saved);
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
