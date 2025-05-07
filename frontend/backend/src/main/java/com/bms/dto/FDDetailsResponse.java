package com.bms.dto;

import com.bms.enums.FDStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing Fixed Deposit details")
public class FDDetailsResponse {

    @Schema(description = "FD account number", example = "FD123456")
    private String fdAccountNumber;

    @Schema(description = "Principal amount", example = "10000.00")
    private BigDecimal principalAmount;

    @Schema(description = "Interest rate", example = "6.5")
    private BigDecimal interestRate;

    @Schema(description = "Term period in months", example = "12")
    private Integer termMonths;

    @Schema(description = "Maturity date")
    private LocalDateTime maturityDate;

    @Schema(description = "Interest earned so far", example = "325.00")
    private BigDecimal interestEarned;

    @Schema(description = "Current status of FD", example = "ACTIVE")
    private FDStatus status;

    @Schema(description = "Creation date")
    private LocalDateTime createdAt;
} 