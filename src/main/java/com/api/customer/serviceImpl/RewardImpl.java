package com.api.customer.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.customer.model.Transaction;
import com.api.customer.repository.TransactionRepository;
import com.api.customer.service.Reward;

@Service
public class RewardImpl implements Reward {
	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public Transaction createTransaction(Transaction transaction) {

		return transactionRepository.save(transaction);
	}

	@Override
	public List<Transaction> getAllTransactions() {

		return transactionRepository.findAll();
	}

	@Override
	public Map<Long, Map<String, Object>> calculateRewards() {
		List<Transaction> transactions = transactionRepository.findAll();

		Map<Long, Map<String, Object>> customerRewards = new HashMap<>();

		for (Transaction t : transactions) {
			Long customerId = t.getCustomerId();
			int points = calculatePoints(t.getAmount());

			// Format month as YYYY-MM
			String month = t.getTransactionDate().getYear() + "-"
					+ String.format("%02d", t.getTransactionDate().getMonthValue());

			customerRewards.putIfAbsent(customerId, new HashMap<>());
			Map<String, Object> rewardsData = customerRewards.get(customerId);

			// Monthly points
			Map<String, Integer> monthly = (Map<String, Integer>) rewardsData.getOrDefault("monthly", new HashMap<>());
			monthly.put(month, monthly.getOrDefault(month, 0) + points);

			// Total points
			int totalReward = (int) rewardsData.getOrDefault("total", 0);
			rewardsData.put("totalReward", totalReward + points);
			rewardsData.put("monthly", monthly);

			customerRewards.put(customerId, rewardsData);
		}

		return customerRewards;
	}

	private int calculatePoints(Double amount) {
		int points = 0;
		if (amount > 100) {
			points += (amount - 100) * 2;
			points += 50; // flat 50 for $50–$100 range
		} else if (amount > 50) {
			points += (amount - 50) * 1;
		}
		return points;
	}

}
