package com.bms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login response containing session information")
public class LoginResponse {

    @Schema(description = "Success message", example = "Login successful")
    private String message;

    @Schema(description = "JWT token for session management", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Token expiration time", example = "2024-04-29T15:30:00")
    private LocalDateTime tokenExpiry;

    @Schema(description = "Account holder's name", example = "John Doe")
    private String accountHolderName;

    @Schema(description = "Account number", example = "BANK123456")
    private String accountNumber;

    @Schema(description = "Current account balance", example = "10000.00")
    private double balance;

    @Schema(description = "Refresh token for session renewal", example = "randomlyGeneratedRefreshTokenHere")
    private String refreshToken;

    @Schema(description = "Refresh token expiration time", example = "2024-05-10T15:30:00")
    private LocalDateTime refreshTokenExpiry;
} 