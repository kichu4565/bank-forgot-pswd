package com.bms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response for token refresh operation")
public class TokenRefreshResponse {
    
    @Schema(description = "Success message", example = "Token refreshed successfully")
    private String message;
    
    @Schema(description = "New JWT token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Token expiration time", example = "2024-04-29T15:30:00")
    private LocalDateTime tokenExpiry;
} 