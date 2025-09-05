package com.api.customer.controller;


import com.api.customer.repository.TransactionRepository;
import com.api.customer.service.RewardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


import java.util.List;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import com.api.customer.model.Transaction;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository repository;
    @Autowired
    private RewardService reward;
    // 1. Add new transaction
    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
    	System.out.println(transaction.getDate());
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

  
   
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Transaction>> getTransactionsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(reward.getTransactionsByCustomerId(customerId));
    }

    @GetMapping("/customer/{customerId}/date-range")
    public ResponseEntity<List<Transaction>> getTransactionsByCustomerAndDateRange(
            @PathVariable Long customerId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reward.getTransactionsByCustomerIdAndDateRange(customerId, startDate, endDate));
    }
}
