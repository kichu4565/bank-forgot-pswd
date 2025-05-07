package com.bms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "Request for creating a new Fixed Deposit")
public class FDCreationRequest {

    @NotNull(message = "Account number is required")
    @Schema(description = "Account number of the user", example = "BANK123456")
    private String accountNumber;

    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "500.00", message = "FD amount must be at least ₹500")
    @Schema(description = "Principal amount for FD", example = "10000.00")
    private BigDecimal principalAmount;

    @NotNull(message = "Term period is required")
    @Min(value = 6, message = "FD duration must be at least 6 months")
    @Schema(description = "Term period in months", example = "12")
    private Integer termMonths;
} 