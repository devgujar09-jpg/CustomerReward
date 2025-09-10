package com.api.customer.repository;

import com.api.customer.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomerId(Long customerId);
    List<Transaction> findByCustomerIdAndDateBetween(Long customerId, LocalDateTime start, LocalDateTime end);
    List<Transaction> findByDateBetween(LocalDateTime start, LocalDateTime end);
}
