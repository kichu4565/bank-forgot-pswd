package com.bms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing Fixed Deposit creation details")
public class FDCreationResponse {

    @Schema(description = "Success message")
    private String message;

    @Schema(description = "FD details")
    private FDDetails fdDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Fixed Deposit details")
    public static class FDDetails {
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

        @Schema(description = "Expected maturity amount", example = "10650.00")
        private BigDecimal expectedMaturityAmount;
    }
} 