package com.bms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(hidden = true)
public class TransactionResponse {
    
    private String transactionId;
    
    private BigDecimal amount;
    
    private String description;
    
    private String status;
    
    private LocalDateTime timestamp;
    
    private BigDecimal sourceAccountBalance;
    
    private String transactionType;
    
    private String sourceAccountNumber;
    private String destinationAccountNumber;

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }
    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }
    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }
    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }
} 