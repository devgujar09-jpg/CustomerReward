package com.api.customer.repository;

import com.api.customer.model.Transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerId(Long customerId);
    List<Transaction> findByCustomerIdAndDateBetween(Long customerId, java.time.LocalDate startDate, java.time.LocalDate endDate);

	@SuppressWarnings("unchecked")
	public Transaction save( Transaction transaction);
	public List<Transaction> findAll();
}
