package com.api.customer.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;


public class TransactionDTO {
    private Long transactionId;
    private Long customerId;
    private Double amount;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
    private Integer rewardPoints;

    public TransactionDTO() {}

    public TransactionDTO(Long transactionId, Long customerId, Double amount, LocalDateTime date, Integer rewardPoints) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
        this.rewardPoints = rewardPoints;
    }

    public Long getTransactionId() { return transactionId; }
    public Long getCustomerId() { return customerId; }
    public Double getAmount() { return amount; }
    public LocalDateTime getDate() { return date; }
    public Integer getRewardPoints() { return rewardPoints; }

    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public void setRewardPoints(Integer rewardPoints) { this.rewardPoints = rewardPoints; }
}
