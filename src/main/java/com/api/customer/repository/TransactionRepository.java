package com.api.customer.repository;

import com.api.customer.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomerId(String customerId);
    List<Transaction> findByCustomerIdAndTransactionDateBetween(String customerId, Instant start, Instant end);
    List<Transaction> findByTransactionDateBetween(Instant start, Instant end);
}
