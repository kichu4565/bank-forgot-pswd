package com.bms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description = "Request for downloading account statement")
public class StatementRequest {
    
    @NotNull(message = "From date is required")
    @Schema(description = "Start date for statement (YYYY-MM-DD)", example = "2024-01-01")
    private LocalDate fromDate;
    
    @NotNull(message = "To date is required")
    @Schema(description = "End date for statement (YYYY-MM-DD)", example = "2024-03-31")
    private LocalDate toDate;
} 