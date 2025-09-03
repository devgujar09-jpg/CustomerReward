package com.api.customer.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.api.customer.model.Transaction;

public interface Reward {
	public Transaction createTransaction(@RequestBody Transaction transaction);
	public List<Transaction> getAllTransactions();
	public Map<Long, Map<String, Object>> calculateRewards();
}

