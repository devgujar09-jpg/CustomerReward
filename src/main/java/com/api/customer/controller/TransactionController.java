package com.api.customer.controller;

import com.api.customer.model.Transaction;
import com.api.customer.repository.TransactionRepository;
import com.api.customer.service.Reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository repository;
    @Autowired
    private Reward reward;
    // 1. Add new transaction
    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return reward.createTransaction(transaction);
    }

    //  2. Get all transactions
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return reward.getAllTransactions();
    }

    // 3. Rewards 
    @GetMapping("/rewards")
    public Map<Long, Map<String, Object>> calculateRewards() {
        return reward.calculateRewards();
        }

  
   
}
