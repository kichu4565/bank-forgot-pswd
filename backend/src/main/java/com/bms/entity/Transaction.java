package com.bms.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;
    
    @Column(name = "source_account_number", nullable = false)
    private String sourceAccountNumber;
    
    @Column(name = "destination_account_number", nullable = false)
    private String destinationAccountNumber;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    private String description;
    
    @Column(nullable = false)
    private String status;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "source_account_balance", nullable = false)
    private BigDecimal sourceAccountBalance;
    
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
} 