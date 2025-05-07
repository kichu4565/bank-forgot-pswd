package com.bms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(hidden = true)
public class DashboardResponse {
    
    @Schema(description = "Account details")
    private AccountDetails accountDetails;
    
    @Schema(description = "Transaction summary")
    private TransactionSummary transactionSummary;
    
    @Schema(description = "Recent transactions")
    private List<TransactionSummary> recentTransactions;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Account details information")
    public static class AccountDetails {
        @Schema(description = "Account number")
        private String accountNumber;
        
        @Schema(description = "Account holder's name")
        private String accountHolderName;
        
        @Schema(description = "Current balance")
        private BigDecimal balance;
        
        @Schema(description = "Account type")
        private String accountType;
        
        @Schema(description = "Account status")
        private String status;
        
        @Schema(description = "Account creation date")
        private LocalDateTime createdAt;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Transaction summary information")
    public static class TransactionSummary {
        @Schema(description = "Transaction ID")
        private String transactionId;
        
        @Schema(description = "Transaction type (DEBIT/CREDIT)")
        private String type;
        
        @Schema(description = "Transaction amount")
        private BigDecimal amount;
        
        @Schema(description = "Transaction timestamp")
        private LocalDateTime timestamp;
        
        @Schema(description = "Transaction status")
        private String status;
    }
} 