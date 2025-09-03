package com.api.customer.repository;

import com.api.customer.model.Transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestBody;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	@SuppressWarnings("unchecked")
	public Transaction save(@RequestBody Transaction transaction);
	public List<Transaction> findAll();
}
