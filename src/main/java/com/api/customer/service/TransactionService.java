package com.api.customer.service;

import com.api.customer.dto.TransactionDTO;
import com.api.customer.model.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    List<TransactionDTO> getTransactionsByCustomerId(Long customerId);
    List<TransactionDTO> getTransactionsByCustomerIdAndDateRange(Long customerId, LocalDate startDate, LocalDate endDate);
    TransactionDTO createTransaction(TransactionDTO transactionDto);
}