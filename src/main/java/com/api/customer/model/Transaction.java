package com.api.customer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private Double amount;
    private LocalDateTime date;

    public Transaction() {}

    public Transaction(Long customerId, Double amount, LocalDateTime date) {
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
    }

    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public Double getAmount() { return amount; }
    public LocalDateTime getDate() { return date; }

    public void setId(Long id) { this.id = id; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
