package com.bms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard error response")
public class ErrorResponse {
    
    @Schema(description = "Error message describing what went wrong")
    private String message;
    
    @Schema(description = "HTTP status code")
    private int status;
    
    @Schema(description = "Error code for more specific error identification")
    private String errorCode;
    
    @Schema(description = "Timestamp when the error occurred")
    private long timestamp;
} 