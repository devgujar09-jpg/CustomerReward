package com.api.customer.controller;

import com.api.customer.dto.TransactionDTO;
import com.api.customer.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO> create(@Valid @RequestBody TransactionDTO dto) {
        return new ResponseEntity<>(transactionService.createTransaction(dto), HttpStatus.CREATED);
    }

    @GetMapping("/customers/{customerId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(transactionService.getTransactionsByCustomerId(customerId));
    }

    @GetMapping(value = "/customers/{customerId}/transactions", params = { "startDate", "endDate" })
    public ResponseEntity<List<TransactionDTO>> getByCustomerAndDateRange(
            @PathVariable Long customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(transactionService.getTransactionsByCustomerIdAndDateRange(customerId, startDate, endDate));
    }
}
