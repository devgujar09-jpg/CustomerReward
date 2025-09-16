package com.api.customer.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class TransactionDTO {
    private Long id;
    private String customerId;
    private double amount;
    private Instant transactionDate;
    private int points; // added field to match service logic
}
