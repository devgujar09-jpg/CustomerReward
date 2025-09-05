package com.api.customer.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.api.customer.model.Transaction;

public interface RewardService {

    java.util.List<com.api.customer.model.Transaction> getTransactionsByCustomerId(Long customerId);
    java.util.List<com.api.customer.model.Transaction> getTransactionsByCustomerIdAndDateRange(Long customerId, java.time.LocalDate startDate, java.time.LocalDate endDate);

	public Transaction createTransaction(@RequestBody Transaction transaction);
	public List<Transaction> getAllTransactions();
	public Map<Long, Map<String, Object>> calculateRewards();
}

