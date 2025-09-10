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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
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
    public java.util.List<TransactionDTO> getTransactionsByCustomerId(Long customerId) {
        try {
            List<Transaction> txs = repository.findByCustomerId(customerId);
            if (txs == null || txs.isEmpty()) throw new NotFoundException("Customer not found or no transactions");
            return txs.stream().map(this::mapToTransactionDTO).collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new RuntimeException("DB error", e);
        }
    }

    @Override
    public java.util.List<TransactionDTO> getTransactionsByCustomerIdAndDateRange(Long customerId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) throw new InvalidDateRangeException("startDate must be before or equal to endDate");
        LocalDateTime s = startDate.atStartOfDay();
        LocalDateTime e = endDate.atTime(LocalTime.MAX);
        try {
            List<Transaction> txs = repository.findByCustomerIdAndDateBetween(customerId, s, e);
            if (txs == null || txs.isEmpty()) throw new NotFoundException("Customer not found or no transactions in range");
            return txs.stream().map(this::mapToTransactionDTO).collect(Collectors.toList());
        } catch (DataAccessException ex) {
            throw new RuntimeException("DB error", ex);
        }
    }

    @Override
    public TransactionDTO createTransaction(TransactionDTO dto) {
        Transaction t = new Transaction(dto.getCustomerId(), dto.getAmount(), dto.getDate());
        Transaction saved = repository.save(t);
        return mapToTransactionDTO(saved);
    }

    private TransactionDTO mapToTransactionDTO(Transaction t) {
        int points = calculateRewardPoints(t.getAmount());
        return new TransactionDTO(t.getId(), t.getCustomerId(), t.getAmount(), t.getDate(), points);
    }

    // Reward rule: 1 point per dollar over 50, plus 2 per dollar over 100
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
