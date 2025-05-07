package com.bms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request object for performing transactions")
public class TransactionRequest {
    
    @Schema(description = "Source account number (will be set automatically from logged-in user)", example = "1234567890")
    private String sourceAccountNumber;
    
    @NotBlank(message = "Destination account number is required")
    @Schema(description = "Destination account number", example = "0987654321")
    private String destinationAccountNumber;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Schema(description = "Transaction amount", example = "1000.00")
    private BigDecimal amount;
    
    @Schema(description = "Transaction description", example = "Monthly rent payment")
    private String description;
} 